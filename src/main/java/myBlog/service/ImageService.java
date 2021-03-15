package myBlog.service;

import static java.lang.String.format;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import myBlog.exeption.FileTypeException;

@Service
public class ImageService {

  public String saveImage(MultipartFile image, HttpServletRequest request, int width) throws IOException, InterruptedException {
    BufferedImage originalImage = ImageIO.read(image.getInputStream());

    String contentType = Objects.requireNonNull(image.getContentType()).substring(image.getContentType().lastIndexOf('/') + 1);
    if (contentType.equals("jpeg") || contentType.equals("png")) {
      String path = format("upload/%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphabetic(2).toLowerCase()) +
          image.getOriginalFilename();
      String realPath = request.getServletContext().getRealPath(path);
      FileUtils.writeByteArrayToFile(new File(realPath), toByteArrayAutoClosable(Scalr.resize(originalImage, width)));
      return "/" + path;
    } else {
      throw new FileTypeException();
    }
  }

  private static byte[] toByteArrayAutoClosable(BufferedImage image) throws IOException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      ImageIO.write(image, "jpg", out);
      return out.toByteArray();
    }
  }
}