package myBlog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import myBlog.annotation.UserId;
import myBlog.api.request.CommentRequest;
import myBlog.api.request.ProfileRequest;
import myBlog.api.response.CalendarResponse;
import myBlog.api.response.CommentResponse;
import myBlog.api.response.InitResponse;
import myBlog.api.response.MainResponse;
import myBlog.api.response.PostResponse;
import myBlog.api.response.StatisticResponse;
import myBlog.api.response.TagResponse;
import myBlog.dto.GlobalSettingsDto;
import myBlog.service.CalendarService;
import myBlog.service.CommentService;
import myBlog.service.GlobalSettingsService;
import myBlog.service.ImageService;
import myBlog.service.PostService;
import myBlog.service.TagService;
import myBlog.service.UserService;

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
  private final UserService userService;

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


  @PutMapping("/settings")
  private void setSettings(@RequestBody GlobalSettingsDto globalSettingsDto) {
    settingsService.setGlobalSettings(globalSettingsDto);
  }


  @GetMapping("/init")
  private ResponseEntity<InitResponse> init() {
    return ResponseEntity.ok(initResponse);
  }

  @GetMapping("/calendar")
  private ResponseEntity<CalendarResponse> getPostByYear(String year) {
    return ResponseEntity.ok(calendarService.getPostByYear(year));
  }

  @PostMapping(path = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  private String postImage(@RequestBody MultipartFile image, HttpServletRequest request) throws Exception {
    return imageService.saveImage(image, request);
  }

  @PostMapping("/comment")
  private ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest,
      @UserId Integer userId) {
    return ResponseEntity.ok(commentService.addComment(commentRequest, userId));
  }

  @PostMapping(path = "/profile/my", consumes = {MediaType.APPLICATION_JSON_VALUE})
  private ResponseEntity<MainResponse> editProfile(@Valid @RequestBody ProfileRequest profileRequest,
      @UserId Integer userId) {
    return ResponseEntity.ok(userService.editProfile(profileRequest, userId));
  }

  @PostMapping(value = "/profile/my", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  private ResponseEntity<ProfileRequest> editAvatarProfile(@Valid @RequestBody ProfileRequest profileRequest,
      @UserId Integer userId) {
    return ResponseEntity.ok(profileRequest);
  }

  @GetMapping("/statistics/my")
  private ResponseEntity<StatisticResponse> getMyStatistic(@UserId Integer userId) {
    return ResponseEntity.ok(userService.getMyStatistic(userId));
  }

  @GetMapping("/statistics/all")
  private ResponseEntity<StatisticResponse> getAllStatistic() {
    StatisticResponse statisticResponse = userService.getAllStatistic();
    if (statisticResponse == null) {
      return ResponseEntity.status(401).build();
    } else {
      return ResponseEntity.ok(statisticResponse);
    }
  }
}


