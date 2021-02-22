package myBlog.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.api.response.CaptchaCodeResponse;
import myBlog.model.CaptchaCode;
import myBlog.repository.CaptchaCodeRepository;

@Service
public class CaptchaService {

  private final CaptchaCodeResponse captchaCodeResponse = new CaptchaCodeResponse();

  @Autowired
  CaptchaCodeRepository captchaCodeRepository;

  public CaptchaCodeResponse getCaptchaCode() {
    captchaCodeRepository.deleteOldCaptchaCode();
    Cage cage = new GCage();
    String captcha = cage.getTokenGenerator().next();
    String secret = cage.getTokenGenerator().next();
    byte[] decodedBytes = cage.draw(captcha);
    String encodedString = Base64.getEncoder().encodeToString(decodedBytes);
    captchaCodeResponse.setSecret(captcha);
    captchaCodeResponse.setImage("data:image/png;base64, " + encodedString);

    captchaCodeRepository.save(CaptchaCode.builder()
        .generationTime(LocalDateTime.now())
        .code(captcha)
        .secretCode(secret)
        .build());

    return captchaCodeResponse;
  }

}
