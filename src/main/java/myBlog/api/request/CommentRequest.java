package myBlog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {


  @JsonProperty("parent_id")
  private Integer parentId;

  @JsonProperty("post_id")
  private int postId;

  private String text;
}
