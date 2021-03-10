package myBlog.service;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import myBlog.api.response.MainResponse;
import myBlog.exeption.FileTypeException;

@Service
public class ImageService {

  private final MainResponse mainResponse = new MainResponse();

  public String saveImage(MultipartFile image) throws IOException {

    String contentType = Objects.requireNonNull(image.getContentType()).substring(image.getContentType().lastIndexOf('/') + 1);
    if (contentType.equals("jpeg") || contentType.equals("png")) {
      String prePath = "src/main/resources/static";
      String path = format("/upload/%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          image.getOriginalFilename();

      FileUtils.writeByteArrayToFile(new File(prePath + path), image.getBytes());
      return path;
    } else {
      throw new FileTypeException();
    }
  }
}
