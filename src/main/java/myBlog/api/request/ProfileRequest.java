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
  @Nullable
  private String password;
  @Nullable
  private String removePhoto;
  @Nullable
  private String photo;

  public ProfileRequest(String name, String email) {
    this.name = name;
    this.email = email;
    password = null;
    removePhoto = null;
    photo = null;
  }

}
