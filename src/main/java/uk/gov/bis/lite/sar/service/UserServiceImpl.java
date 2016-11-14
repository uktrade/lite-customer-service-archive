package uk.gov.bis.lite.sar.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.common.item.in.UserRoleIn;
import uk.gov.bis.lite.common.item.out.UserOut;
import uk.gov.bis.lite.common.item.out.UsersOut;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.sar.spire.SpireReferenceClient;
import uk.gov.bis.lite.sar.spire.SpireUserDetailClient;
import uk.gov.bis.lite.sar.spire.model.SpireUserDetail;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Singleton
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  public static final String USER_ROLE_UPDATE_STATUS_COMPLETE = "COMPLETE";
  public static final String USER_ROLE_UPDATE_STATUS_ERROR = "Error";

  public static final String SPIRE_ROLE_APPLICATION_CONTACT = "APPLICATION_CONTACT";
  public static final String SPIRE_ROLE_APPLICATION_PREPARER = "APPLICATION_PREPARER";
  public static final String SPIRE_ROLE_APPLICATION_SUBMITTER = "APPLICATION_SUBMITTER";
  public static final String SPIRE_ROLE_LICENCE_RETURN_CONTACT = "LICENCE_RETURN_CONTACT";
  public static final String SPIRE_ROLE_SAR_ADMINISTRATOR = "SAR_ADMINISTRATOR";

  private SpireReferenceClient editUserRolesClient;
  private SpireUserDetailClient userDetailClient;

  @Inject
  public UserServiceImpl(@Named("SpireEditUserRolesClient") SpireReferenceClient editUserRolesClient,
                         SpireUserDetailClient userDetailClient) {
    this.editUserRolesClient = editUserRolesClient;
    this.userDetailClient = userDetailClient;
  }

  public String userRoleUpdate(UserRoleIn item, String userId, String siteRef) {
    SpireRequest request = editUserRolesClient.createRequest();
    request.addChild("VERSION_NO", "1.1");
    request.addChild("ADMIN_WUA_ID", item.getAdminUserId());
    request.addChild("USER_WUA_ID", userId);
    request.addChild("SITE_REF", siteRef);
    request.addChild("ROLE_TYPE", item.getRoleType());
    return editUserRolesClient.sendRequest(request);
  }

  public UsersOut getCustomerAdminUsers(String customerId) {
    SpireRequest request = userDetailClient.createRequest();
    request.addChild("sarRef", customerId);
    List<SpireUserDetail> spireUserDetails = userDetailClient.sendRequest(request);
    List<UserOut> adminUserDetails = spireUserDetails.stream()
        .filter(sud -> sud.getRoleName().equals(SPIRE_ROLE_SAR_ADMINISTRATOR))
        .map(siteOutFunction)
        .collect(Collectors.toList());

    LOGGER.info("adminUserDetails: " + adminUserDetails.size());
    return new UsersOut(adminUserDetails);
  }

  /**
   * Maps SpireUserDetail to UserOut
   */
  private static final Function<SpireUserDetail, UserOut> siteOutFunction = spireUserDetail -> {
    UserOut userOut = new UserOut();
    userOut.setEmailAddress(spireUserDetail.getEmailAddress());
    userOut.setUserId(spireUserDetail.getUserId());
    userOut.setForename(spireUserDetail.getForename());
    userOut.setFullName(spireUserDetail.getFullName());
    userOut.setRoleName(spireUserDetail.getRoleName());
    return userOut;
  };

}