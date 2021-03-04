package myBlog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentResponse {

  @JsonProperty("id")
  private int commentId;

}
