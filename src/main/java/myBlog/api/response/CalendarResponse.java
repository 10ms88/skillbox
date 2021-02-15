package myBlog.api.response;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CalendarResponse {

  private List<String> years;
  private Map<String, String> posts;
}
