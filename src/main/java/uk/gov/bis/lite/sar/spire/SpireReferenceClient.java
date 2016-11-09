package uk.gov.bis.lite.sar.spire;


import uk.gov.bis.lite.common.spire.client.SpireClient;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

public class SpireReferenceClient extends SpireClient<String> {
  public SpireReferenceClient(SpireParser<String> parser,
                              SpireClientConfig clientConfig,
                              SpireRequestConfig requestConfig) {
    super(parser, clientConfig, requestConfig);
  }
}