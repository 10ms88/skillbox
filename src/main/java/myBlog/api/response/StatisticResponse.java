package myBlog.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticResponse {

  private Integer postsCount;
  private Integer likesCount;
  private Integer dislikesCount;
  private Integer viewsCount;
  private Long firstPublication;

}
