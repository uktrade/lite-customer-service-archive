package uk.gov.bis.lite.customer.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.customer.api.param.AddressParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

  private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

  private static ObjectMapper mapper = new ObjectMapper();

  public static String getBooleanValue(Boolean arg) {
    return arg == null ? "false" : arg ? "true" : "false";
  }

  /**
   * New line delimited address String omitting any nulls or empty
   */
  public static String getFriendlyAddress(AddressParam param) {
    List<String> addressLines = new ArrayList<>(Arrays.asList(param.getLine1(), param.getLine2(), param.getTown(),
        param.getPostcode(), param.getCounty(), param.getCountry()));
    return Joiner.on("\n").join(Iterables.filter(addressLines, StringUtils::isNotBlank));
  }

  public static String getAddressParamJson(AddressParam param) {
    String json = "";
    try {
      json = mapper.writeValueAsString(param).trim();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JsonProcessingException on AddressParam", e);

    }
    return json;
  }
}
