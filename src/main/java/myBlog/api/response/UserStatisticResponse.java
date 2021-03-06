package myBlog.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticResponse {

  private Integer postsCount;
  private Integer likesCount;
  private Integer disLikesCount;
  private Integer viewsCount;
  private Long firstPublication;

}
