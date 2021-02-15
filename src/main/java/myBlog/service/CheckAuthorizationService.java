package myBlog.service;

import org.springframework.stereotype.Service;

import myBlog.api.response.CheckAuthorizationResponse;

@Service
public class CheckAuthorizationService {

  public CheckAuthorizationResponse getCheckAuthorization() {

    CheckAuthorizationResponse checkAuthorizationResponse = new CheckAuthorizationResponse();

    checkAuthorizationResponse.setResult(false);

    return checkAuthorizationResponse;
  }

}
