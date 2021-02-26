package myBlog.api.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class PostRequest {

  private Long timestamp;
  private Integer active;
  private String title;
  private List<String> tags;
  private String captcha;
  private String text;

}
