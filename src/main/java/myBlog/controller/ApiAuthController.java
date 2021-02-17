package myBlog.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.response.CaptchaResponse;
import myBlog.api.response.CheckAuthorizationResponse;
import myBlog.service.CaptchaService;
import myBlog.service.CheckAuthorizationService;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class ApiAuthController {

  private final CheckAuthorizationService checkAuthorizationService;
  private final CaptchaService captchaService;


  @GetMapping("check")
  private ResponseEntity<CheckAuthorizationResponse> checkAuthorizationResponse() {
    return ResponseEntity.ok(checkAuthorizationService.getCheckAuthorization());
  }

  @GetMapping("captcha")
  private ResponseEntity<CaptchaResponse> getCaptchaCode() {
    return ResponseEntity.ok(captchaService.getCaptchaCode());
  }

}
