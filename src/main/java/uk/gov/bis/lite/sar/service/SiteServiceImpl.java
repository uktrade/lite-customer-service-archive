package uk.gov.bis.lite.sar.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.common.item.in.SiteIn;
import uk.gov.bis.lite.common.item.out.AddressOut;
import uk.gov.bis.lite.common.item.out.SiteOut;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.sar.spire.SpireReferenceClient;
import uk.gov.bis.lite.sar.spire.SpireSiteClient;
import uk.gov.bis.lite.sar.spire.model.SpireSite;
import uk.gov.bis.lite.sar.util.Util;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class SiteServiceImpl implements SiteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

  private SpireReferenceClient createSiteForSarReferenceClient;
  private SpireSiteClient siteClient;

  @Inject
  public SiteServiceImpl(@Named("SpireCreateSiteForSarClient") SpireReferenceClient createSiteForSarReferenceClient,
                         SpireSiteClient siteClient) {
    this.createSiteForSarReferenceClient = createSiteForSarReferenceClient;
    this.siteClient = siteClient;
  }

  public String createSite(SiteIn siteIn, String customerId, String userId) {

    if (!StringUtils.isBlank(userId) && siteIn.getAddress() != null) {
      SpireRequest request = createSiteForSarReferenceClient.createRequest();
      request.addChild("VERSION_NO", "1.0");
      request.addChild("WUA_ID", userId);
      request.addChild("SAR_REF", customerId);
      request.addChild("DIVISION", siteIn.getSiteName());
      request.addChild("LITE_ADDRESS", Util.getAddressItemJson(siteIn.getAddress()));
      request.addChild("ADDRESS", Util.getFriendlyAddress(siteIn.getAddress()));
      request.addChild("COUNTRY_REF", siteIn.getAddress().getCountry());
      return createSiteForSarReferenceClient.sendRequest(request);
    } else {
      throw new SpireClientException("Mandatory fields missing: userId and/or address");
    }
  }

  public List<SiteOut> getSites(String customerId, String userId) {
    SpireRequest request = siteClient.createRequest();
    request.addChild("userId", userId);
    request.addChild("sarRef", customerId);
    List<SpireSite> spireSites = siteClient.sendRequest(request);
    return spireSites.stream().map(siteOutFunction).collect(Collectors.toList());
  }

  public Optional<SiteOut> getSite(String siteId) {
    SpireRequest request = siteClient.createRequest();
    request.addChild("siteRef", siteId);
    List<SpireSite> spireSites = siteClient.sendRequest(request);
    if (spireSites.size() > 0) {
      return Optional.of(siteOutFunction.apply(spireSites.get(0)));
    }
    return Optional.empty();
  }

  /**
   * Maps SpireSite to SiteOut
   */
  private static final Function<SpireSite, SiteOut> siteOutFunction = spireSite -> {

    SiteOut siteOut = new SiteOut();
    siteOut.setCustomerId(spireSite.getSarRef());
    siteOut.setSiteId(spireSite.getSiteRef());

    String companyName = spireSite.getCompanyName() != null ? spireSite.getCompanyName() : "";
    String siteName = spireSite.getDivision() != null ? spireSite.getDivision() : companyName;
    siteOut.setSiteName(siteName);

    AddressOut address = new AddressOut();
    address.setPlainText(spireSite.getAddress());
    address.setCountry(spireSite.getCountryRef());

    siteOut.setAddress(address);

    return siteOut;
  };

}
