package myBlog.service;

import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import myBlog.api.response.RegistrationResponse;
import myBlog.exeption.FileTypeException;

@Service
public class ImageService {

  private final RegistrationResponse registrationResponse = new RegistrationResponse();

  public RegistrationResponse saveImage(MultipartFile image) {
    String contentType = Objects.requireNonNull(image.getContentType()).substring(image.getContentType().lastIndexOf('/') + 1);
    if (contentType.equals("jpeg") || contentType.equals("png")) {
      registrationResponse.setResult(true);
      registrationResponse.setErrors(null);
      return registrationResponse;
    } else {
      throw new FileTypeException();
    }
  }
}
