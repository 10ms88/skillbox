package myBlog.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import java.util.Base64;
import org.springframework.stereotype.Service;

import myBlog.api.response.CaptchaCodeResponse;

@Service
public class CaptchaService {

  private final CaptchaCodeResponse captchaCodeResponse = new CaptchaCodeResponse();

  public CaptchaCodeResponse getCaptchaCode() {

    Cage cage = new GCage();
    String captcha = cage.getTokenGenerator().next();
    byte[] decodedBytes = cage.draw(captcha);

    String encodedString = Base64.getEncoder().encodeToString(decodedBytes);

    captchaCodeResponse.setSecret(captcha);
    captchaCodeResponse.setImage("data:image/png;base64, " + encodedString);

    return captchaCodeResponse;
  }

}
