package myBlog.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.web.multipart.MultipartFile;

@Data
@Jacksonized
@Builder
@AllArgsConstructor
public class ProfilePhotoRequest {

  private String name;
  private String email;
  private String password;
  private String removePhoto;
  private MultipartFile photo;

}
