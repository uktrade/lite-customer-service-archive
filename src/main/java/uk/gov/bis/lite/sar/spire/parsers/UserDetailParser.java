package uk.gov.bis.lite.sar.spire.parsers;

import static uk.gov.bis.lite.spire.client.SpireName.EMAIL_ADDRESS;
import static uk.gov.bis.lite.spire.client.SpireName.FORENAME;
import static uk.gov.bis.lite.spire.client.SpireName.FULL_NAME;
import static uk.gov.bis.lite.spire.client.SpireName.ROLE_NAME;
import static uk.gov.bis.lite.spire.client.SpireName.SURNAME;
import static uk.gov.bis.lite.spire.client.SpireName.WUA_ID;

import org.w3c.dom.Node;
import uk.gov.bis.lite.sar.spire.model.SpireUserDetail;
import uk.gov.bis.lite.spire.client.SpireResponse;
import uk.gov.bis.lite.spire.client.parser.SpireParser;

import java.util.ArrayList;
import java.util.List;

public class UserDetailParser implements SpireParser<List<SpireUserDetail>> {

  @Override
  public List<SpireUserDetail> parseResponse(SpireResponse spireResponse) {
    return getUserDetailsFromNodes(spireResponse.getElementChildNodesForList("//SAR_USER_DETAILS_LIST"));
  }

  private List<SpireUserDetail> getUserDetailsFromNodes(List<Node> nodes) {
    List<SpireUserDetail> details = new ArrayList<>();
    for (Node node : nodes) {
      SpireUserDetail detail = new SpireUserDetail();
      SpireResponse.getNodeValue(node, WUA_ID).ifPresent(detail::setUserId);
      SpireResponse.getNodeValue(node, FORENAME).ifPresent(detail::setForename);
      SpireResponse.getNodeValue(node, SURNAME).ifPresent(detail::setSurname);
      SpireResponse.getNodeValue(node, FULL_NAME).ifPresent(detail::setFullName);
      SpireResponse.getNodeValue(node, EMAIL_ADDRESS).ifPresent(detail::setEmailAddress);
      SpireResponse.getNodeValue(node, ROLE_NAME).ifPresent(detail::setRoleName);
      details.add(detail);
    }
    return details;
  }
}
