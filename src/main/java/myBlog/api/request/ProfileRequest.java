package myBlog.api.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

@Data
@Jacksonized
@RequiredArgsConstructor
public class ProfileRequest {

  private String name;
  private String email;
  private String password;
  private String removePhoto;
  private String photo;

}
