package uk.gov.bis.lite.sar.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class Util {

  private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

  public static Optional<String> optionalRef(String ref) {
    if (Util.getRandomBoolean()) {
      return Optional.of(ref);
    }
    return Optional.empty();
  }

  public static void logSoapResponse(SOAPMessage soapMessage) {
    try {
      LOGGER.info("CreateLiteSar response: " + soapMessage.getSOAPBody().getFirstChild().toString());
    } catch (SOAPException e) {
      e.printStackTrace();
    }
  }

  public static boolean isBlank(String arg) {
    return StringUtils.isBlank(arg);
  }

  public static boolean getRandomBoolean() {
    return Math.random() < 0.5;
  }
}
