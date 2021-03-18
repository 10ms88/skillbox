package myBlog.api.response;

import java.util.List;
import lombok.Data;

import myBlog.dto.TagDto;

@Data
public class TagResponse {


  private List<TagDto> tags;
}
