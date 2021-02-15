package myBlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.response.CheckAuthorizationResponse;
import myBlog.service.CheckAuthorizationService;

@RestController
@RequestMapping("/api/auth/")
public class ApiAuthController {

  private final CheckAuthorizationService checkAuthorizationService;


  public ApiAuthController(CheckAuthorizationService checkAuthorizationService) {
    this.checkAuthorizationService = checkAuthorizationService;
  }


  @GetMapping("check")
  private ResponseEntity<CheckAuthorizationResponse> checkAuthorizationResponse() {
    return ResponseEntity.ok(checkAuthorizationService.getCheckAuthorization());
  }
}
