package myBlog.api.response;

import java.util.List;
import lombok.Data;

import myBlog.dto.PostDto;

@Data
public class PostResponse {

  private Long count;
  private List<PostDto> posts;
}
