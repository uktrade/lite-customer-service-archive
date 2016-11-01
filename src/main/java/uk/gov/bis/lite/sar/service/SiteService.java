package uk.gov.bis.lite.sar.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.sar.model.Site;
import uk.gov.bis.lite.sar.model.SiteItem;
import uk.gov.bis.lite.sar.spire.SpireReferenceClient;
import uk.gov.bis.lite.sar.spire.SpireSiteClient;
import uk.gov.bis.lite.sar.util.Util;
import uk.gov.bis.lite.spire.client.SpireName;
import uk.gov.bis.lite.spire.client.SpireRequest;
import uk.gov.bis.lite.spire.client.exception.SpireException;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SiteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SiteService.class);

  private SpireReferenceClient createSiteForSarReferenceClient;
  private SpireSiteClient siteClient;

  @Inject
  public SiteService(@Named("SpireCreateSiteForSarClient") SpireReferenceClient createSiteForSarReferenceClient,
                     SpireSiteClient siteClient) {
    this.createSiteForSarReferenceClient = createSiteForSarReferenceClient;
    this.siteClient = siteClient;
  }

  public String createSite(SiteItem item) {
    if (!StringUtils.isBlank(item.getUserId()) && item.getAddressItem() != null) {
      SpireRequest request = createSiteForSarReferenceClient.createRequest();
      request.addChild(SpireName.VERSION_NO, SpireName.VERSION_1_0);
      request.addChild(SpireName.WUA_ID, item.getUserId());
      request.addChild(SpireName.SAR_REF, item.getSarRef());
      request.addChild(SpireName.DIVISION, item.getSiteName());
      request.addChild(SpireName.LITE_ADDRESS, Util.getAddressItemJson(item.getAddressItem()));
      request.addChild(SpireName.ADDRESS, Util.getFriendlyAddress(item.getAddressItem()));
      request.addChild(SpireName.COUNTRY_REF, item.getAddressItem().getCountry());
      return createSiteForSarReferenceClient.getResult(request);
    } else {
      throw new SpireException("Mandatory fields missing: userId and/or address");
    }
  }

  public List<Site> getSites(String customerId, String userId) {
    SpireRequest request = siteClient.createRequest();
    request.addChild(SpireName.userId, userId);
    request.addChild(SpireName.sarRef, customerId);
    return siteClient.getResult(request).stream().map(Site::new).collect(Collectors.toList());
  }
}
