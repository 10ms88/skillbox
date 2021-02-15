package myBlog.api.request;

import java.util.List;
import lombok.Data;

@Data
public class SorterPostRequest {

  private List<Integer> commentCounts;
  private List<Integer> likeCounts;
}
