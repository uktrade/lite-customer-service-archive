package uk.gov.bis.lite.sar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sarRef", "siteRef", "companyName", "applicantType", "division",
    "address"})
public class SiteWrapper {

  private Site site;

  public SiteWrapper(Site site) {
    this.site = site;
  }

  @JsonProperty("sarRef")
  public String sarRef() {
    return site.getSarRef();
  }

  @JsonProperty("siteRef")
  public String siteRef() {
    return site.getSiteRef();
  }

  @JsonProperty("companyName")
  public String companyName() {
    return site.getCompanyName();
  }

  @JsonProperty("applicantType")
  public String applicantType() {
    return site.getApplicantType();
  }

  @JsonProperty("division")
  public String division() {
    return site.getDivision();
  }

  @JsonProperty("address")
  public String address() {
    return site.getAddress();
  }
}