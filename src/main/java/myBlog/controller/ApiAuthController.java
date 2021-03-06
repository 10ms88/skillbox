package myBlog.controller;

import java.security.Principal;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.request.ChangePasswordRequest;
import myBlog.api.request.LoginRequest;
import myBlog.api.request.ProfileRequest;
import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.CaptchaCodeResponse;
import myBlog.api.response.LoginResponse;
import myBlog.api.response.MainResponse;
import myBlog.service.CaptchaService;
import myBlog.service.UserService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {


  private final CaptchaService captchaService;
  private final UserService userService;


  @GetMapping("/check")
  private ResponseEntity<LoginResponse> checkAuthorizationResponse(Principal principal) {
    if (principal == null) {
      return ResponseEntity.ok(new LoginResponse());
    }
    return ResponseEntity.ok(userService.getLoginResponseFromEmail(principal.getName()));
  }

  @GetMapping("/captcha")
  private ResponseEntity<CaptchaCodeResponse> getCaptchaCode() {
    return ResponseEntity.ok(captchaService.getCaptchaCode());
  }

  @PostMapping("/register")
  private ResponseEntity<MainResponse> userRegistration(
      @RequestBody RegistrationRequest registrationRequest
  ) {

    return ResponseEntity.ok(userService.createUser(registrationRequest));
  }

  @PostMapping("/login")
  private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(userService.setAuthentication(loginRequest));
  }

  @GetMapping("/logout")
  private ResponseEntity<LoginResponse> logout(HttpServletRequest request) throws Exception {
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();
    session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    for (Cookie cookie : request.getCookies()) {
      cookie.setMaxAge(0);
    }
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);

    return ResponseEntity.ok(loginResponse);
  }


  @PostMapping("/restore")
  private ResponseEntity<MainResponse> restorePassword(@RequestBody ProfileRequest profileRequest,
      HttpServletRequest request) {
    return ResponseEntity.ok(userService.restorePassword(profileRequest.getEmail(), request));
  }


  @PostMapping("/password")
  private ResponseEntity<MainResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
      HttpServletRequest request) {
    return ResponseEntity.ok(userService.changePassword(changePasswordRequest));
  }


}
