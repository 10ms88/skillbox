package myBlog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ChangePasswordRequest {

  private String code;

  private String password;

  private String captcha;

  @JsonProperty("captcha_secret")
  private String captchaSecret;

}
