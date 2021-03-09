package myBlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import myBlog.model.Post;

@Data
@AllArgsConstructor
public class GlobalSettingsDto {

  @JsonProperty("MULTIUSER_MODE")
  private boolean MULTIUSER_MODE;

  @JsonProperty("POST_PREMODERATION")
  private boolean POST_PREMODERATION;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean STATISTICS_IS_PUBLIC;
}
