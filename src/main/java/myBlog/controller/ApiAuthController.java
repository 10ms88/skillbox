package myBlog.controller;

import java.security.Principal;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.request.LoginRequest;
import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.CaptchaCodeResponse;
import myBlog.api.response.LoginResponse;
import myBlog.api.response.RegistrationResponse;
import myBlog.service.CaptchaService;
import myBlog.service.UserService;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class ApiAuthController {


  private final CaptchaService captchaService;
  private final UserService userService;


  @GetMapping("check")
  private ResponseEntity<LoginResponse> checkAuthorizationResponse(Principal principal) {
    if (principal == null) {
      return ResponseEntity.ok(new LoginResponse());
    }
    return ResponseEntity.ok(userService.getLoginResponseFromEmail(principal.getName()));
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

  @PostMapping("login")
  private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(userService.setAuthentication(loginRequest));
  }

  @GetMapping("logout")
  private ResponseEntity<LoginResponse> logout() {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    return ResponseEntity.ok(loginResponse);
  }
}
