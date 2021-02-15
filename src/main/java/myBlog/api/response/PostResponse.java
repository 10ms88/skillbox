package myBlog.api.response;

import java.util.List;
import lombok.Data;

import myBlog.dto.PostDto;
import myBlog.model.Post;

@Data
public class PostResponse {

    private int count;

    private List<PostDto> posts;


}
