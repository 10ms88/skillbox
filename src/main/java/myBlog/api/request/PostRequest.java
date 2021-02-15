package myBlog.api.request;

import lombok.Data;

@Data
public class PostRequest {

    private int offset;
    private int limit;
    private String mode;

}
