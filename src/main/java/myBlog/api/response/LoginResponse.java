package myBlog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {

  private boolean result;

  @JsonProperty("user")
  @JsonInclude(Include.NON_NULL)
  private UserLoginResponse userLoginResponse;
}
