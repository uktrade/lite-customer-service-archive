package uk.gov.bis.lite.customer.service;

import uk.gov.bis.lite.customer.api.UsersResponse;
import uk.gov.bis.lite.customer.api.param.UserRoleParam;

public interface UserService {

  String userRoleUpdate(UserRoleParam param, String userId, String siteRef);

  UsersResponse getCustomerAdminUsers(String customerId);
}
