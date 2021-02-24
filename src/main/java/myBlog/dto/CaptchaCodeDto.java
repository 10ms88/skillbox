package myBlog.dto;

import lombok.Builder;
import lombok.Data;

import myBlog.model.CaptchaCode;

@Data
@Builder
public class CaptchaCodeDto {

  private final String secret;
  private final String image;

  public static CaptchaCodeDto of(CaptchaCode captchaCode) {
    return CaptchaCodeDto.builder()
        .secret(captchaCode.getSecretCode())
        .build();
  }


}
