package myBlog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import myBlog.annotation.UserEmail;
import myBlog.api.request.CommentRequest;
import myBlog.api.response.CalendarResponse;
import myBlog.api.response.CommentResponse;
import myBlog.api.response.InitResponse;
import myBlog.api.response.PostResponse;
import myBlog.api.response.TagResponse;
import myBlog.dto.GlobalSettingsDto;
import myBlog.service.CalendarService;
import myBlog.service.CommentService;
import myBlog.service.GlobalSettingsService;
import myBlog.service.ImageService;
import myBlog.service.PostService;
import myBlog.service.TagService;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class ApiGeneralController {

  private final GlobalSettingsService settingsService;
  private final InitResponse initResponse;
  private final PostService postService;
  private final TagService tagService;
  private final CalendarService calendarService;
  private final ImageService imageService;
  private final CommentService commentService;


  @GetMapping("/tag")
  private ResponseEntity<TagResponse> tagResponseEntity(@RequestParam(defaultValue = "") String query) {
    return ResponseEntity.ok(tagService.getTagList(query));
  }


  @GetMapping("/post")
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

    String uploadDirectory = System.getProperty("user.dir") + "/uploads";
    return ResponseEntity.ok(calendarService.getPostByYear(year));
  }

  @PostMapping("/image")
  private String postImage(@RequestParam MultipartFile image) throws Exception {
    return imageService.saveImage(image);
  }

  @PostMapping("/comment")
  private ResponseEntity<CommentResponse> postImage(@RequestBody CommentRequest commentRequest,
      @UserEmail String userEmail) {
    return ResponseEntity.ok(commentService.addComment(commentRequest, userEmail));
  }

}


