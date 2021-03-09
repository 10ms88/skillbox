package myBlog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import myBlog.annotation.UserEmail;
import myBlog.api.request.PostVoteRequest;
import myBlog.api.request.ModerationRequest;
import myBlog.api.request.PostRequest;
import myBlog.api.response.PostResponse;
import myBlog.api.response.MainResponse;
import myBlog.dto.PostIdDto;
import myBlog.service.PostVoteService;
import myBlog.service.PostService;

@RestController
@RequestMapping("/api")
public class ApiPostController {

  private final PostService postService;
  private final PostVoteService postVoteService;


  public ApiPostController(PostService postService, PostVoteService postVoteService) {
    this.postService = postService;
    this.postVoteService = postVoteService;
  }


  @GetMapping("/post/search")
  private ResponseEntity<PostResponse> getSearchedPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String query) {

    Pageable pageable = PageRequest.of(offset / 10, limit);

    return ResponseEntity.ok(postService.getSearchedPosts(pageable, query));
  }

  @GetMapping("/post/byDate")
  private ResponseEntity<PostResponse> getPostsByDate(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String date) {

    Pageable pageable = PageRequest.of(offset / 10, limit);

    return ResponseEntity.ok(postService.getPostsByDate(pageable, date));
  }

  @GetMapping("/post/byTag")
  private ResponseEntity<PostResponse> getPostsByTag(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String tag) {
    Pageable pageable = PageRequest.of(offset / 10, limit);
    return ResponseEntity.ok(postService.getPostsByTag(pageable, tag));
  }

  @GetMapping("/post/{id}")
  private ResponseEntity<PostIdDto> getPostsById(
      @PathVariable("id") int id) {
    return ResponseEntity.ok(postService.getPostsById(id));
  }

  @GetMapping("/post/moderation")
  private ResponseEntity<PostResponse> getModerationPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String status,
      @UserEmail String userEmail
  ) {
    Pageable pageable = PageRequest.of(offset / 10, limit);
    return ResponseEntity.ok(postService.getModerationPosts(pageable, status, userEmail));
  }

  @GetMapping("/post/my")
  private ResponseEntity<PostResponse> getMyPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(required = false, defaultValue = "") String status,
      @UserEmail String userEmail
  ) {
    Pageable pageable = PageRequest.of(offset / 10, limit);
    return ResponseEntity.ok(postService.getMyPosts(pageable, status, userEmail));
  }

  @PostMapping("/post")
  private ResponseEntity<MainResponse> addPost(
      @RequestBody PostRequest postRequest,
      @UserEmail String userEmail
  ) {
    return ResponseEntity.ok(postService.createPost(postRequest, userEmail));
  }

  @PutMapping("/post/{postId}")
  private ResponseEntity<MainResponse> updatePost(
      @RequestBody PostRequest postRequest,
      @UserEmail String userEmail,
      @PathVariable Integer postId
  ) {
    return ResponseEntity.ok(postService.updatePost(postRequest, userEmail, postId));
  }

  @PostMapping("/moderation")
  private ResponseEntity<MainResponse> moderatePost(
      @RequestBody ModerationRequest moderationRequest,
      @UserEmail String userEmail
  ) {
    return ResponseEntity.ok(postService.moderatePost(moderationRequest, userEmail));
  }

  @PostMapping("/post/like")
  private ResponseEntity<MainResponse> addLike(
      @RequestBody PostVoteRequest postVoteRequest,
      @UserEmail String userEmail
  ) {
    return ResponseEntity.ok(postVoteService.addLike(postVoteRequest, userEmail));
  }

  @PostMapping("/post/dislike")
  private ResponseEntity<MainResponse> addDislike(
      @RequestBody PostVoteRequest postVoteRequest,
      @UserEmail String userEmail
  ) {
    return ResponseEntity.ok(postVoteService.addDislike(postVoteRequest, userEmail));
  }
}
