package myBlog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Data;

@Data
public class MainResponse {

  private boolean result;

  @JsonInclude(Include.NON_NULL)
  private Map<String, String> errors;
}
