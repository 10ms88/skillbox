package myBlog.api.response;

import lombok.Data;

import myBlog.dto.TagDto;
import myBlog.model.Tag;

import java.util.List;

@Data
public class TagResponse {

    private List<TagDto> tags;
}
