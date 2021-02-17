package myBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GlobalSettingsDto {

  private final boolean MULTIUSER_MODE;
  private final boolean POST_PREMODERATION;
  private final boolean STATISTICS_IS_PUBLIC;
}
