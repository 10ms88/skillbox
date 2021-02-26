package myBlog.controller;

import static java.lang.String.format;

import java.io.File;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import myBlog.api.response.CalendarResponse;
import myBlog.api.response.InitResponse;
import myBlog.api.response.PostResponse;
import myBlog.api.response.TagResponse;
import myBlog.dto.GlobalSettingsDto;
import myBlog.service.CalendarService;
import myBlog.service.GlobalSettingsService;
import myBlog.service.ImageService;
import myBlog.service.PostService;
import myBlog.service.TagService;

@RestController
@RequestMapping(path = "/api/")
@AllArgsConstructor
public class ApiGeneralController {

  private final GlobalSettingsService settingsService;
  private final InitResponse initResponse;
  private final PostService postService;
  private final TagService tagService;
  private final CalendarService calendarService;
  private final ImageService imageService;


  @GetMapping("/tag")
  private ResponseEntity<TagResponse> tagResponseEntity(@RequestParam(defaultValue = "") String query) {
    return ResponseEntity.ok(tagService.getTagList(query));
  }


  @GetMapping("post")
  private ResponseEntity<PostResponse> getSearchedPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "recent") String mode) {

    Pageable pageable = PageRequest.of(offset / 10, limit);

    return ResponseEntity.ok(postService.getFilteredPosts(pageable, mode));
  }


  @GetMapping("/settings")
  private ResponseEntity<GlobalSettingsDto> settingsResponse() {
    return ResponseEntity.ok(settingsService.getGlobalSettings());
  }


  @GetMapping("/init")
  private ResponseEntity<InitResponse> init() {
    return ResponseEntity.ok(initResponse);
  }

  @GetMapping("/calendar")
  private ResponseEntity<CalendarResponse> getPostByYear(String year) {
    return ResponseEntity.ok(calendarService.getPostByYear(year));
  }


  @PostMapping("/image")
  private ResponseEntity<?> postImage(@RequestParam MultipartFile image) throws Exception {

    if (imageService.saveImage(image).isResult()) {
      String path = format("upload/%s/", RandomStringUtils.randomAlphanumeric(5).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphanumeric(5).toLowerCase()) +
          format("%s/", RandomStringUtils.randomAlphanumeric(5).toLowerCase()) +
          image.getOriginalFilename();
      FileUtils.writeByteArrayToFile(new File(path), image.getBytes());
      return ResponseEntity.ok(path);
    } else {
      throw new Exception();
    }
  }
}


