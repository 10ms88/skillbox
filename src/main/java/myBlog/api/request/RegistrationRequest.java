package myBlog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class RegistrationRequest {

  @JsonProperty("e_mail")
  private String email;

  private String password;

  private String name;

  private String captcha;

  @JsonProperty("captcha_secret")
  private String captchaSecret;

}
