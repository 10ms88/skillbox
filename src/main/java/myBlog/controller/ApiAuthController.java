package myBlog.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.CaptchaCodeResponse;
import myBlog.api.response.CheckAuthorizationResponse;
import myBlog.api.response.RegistrationResponse;
import myBlog.service.CaptchaService;
import myBlog.service.CheckAuthorizationService;
import myBlog.service.UserService;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class ApiAuthController {

  private final CheckAuthorizationService checkAuthorizationService;
  private final CaptchaService captchaService;
  private final UserService userService;


  @GetMapping("check")
  private ResponseEntity<CheckAuthorizationResponse> checkAuthorizationResponse() {
    return ResponseEntity.ok(checkAuthorizationService.getCheckAuthorization());
  }

  @GetMapping("captcha")
  private ResponseEntity<CaptchaCodeResponse> getCaptchaCode() {
    return ResponseEntity.ok(captchaService.getCaptchaCode());
  }

  @PostMapping("register")
  private ResponseEntity<RegistrationResponse> userRegistration(
      @RequestBody RegistrationRequest registrationRequest
  ) {
    return ResponseEntity.ok(userService.createUser(registrationRequest));
  }


}
