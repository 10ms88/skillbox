package myBlog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import myBlog.api.response.PostResponse;
import myBlog.service.PostService;

@RestController
@RequestMapping("/api/post/")
public class ApiPostController {

  private final PostService postService;


  public ApiPostController(PostService postService) {
    this.postService = postService;
  }


  @GetMapping("search")
  private ResponseEntity<PostResponse> getSearchedPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String query) {

    Pageable pageable = PageRequest.of(offset, limit);

    return ResponseEntity.ok(postService.getSearchedPosts(pageable, query));
  }
}
