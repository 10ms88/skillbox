package myBlog.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class ProfileRequest {
  private String name;
  private String email;
  private String password;
  private String removePhoto;
  private String photo;

}
