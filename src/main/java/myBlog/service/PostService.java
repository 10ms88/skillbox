package myBlog.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import myBlog.api.request.ModerationRequest;
import myBlog.api.request.PostRequest;
import myBlog.api.response.PostResponse;
import myBlog.api.response.RegistrationResponse;
import myBlog.dto.PostDto;
import myBlog.dto.PostIdDto;
import myBlog.enumuration.ModerationStatus;
import myBlog.model.Post;
import myBlog.model.Tag;
import myBlog.model.User;
import myBlog.repository.PostRepository;
import myBlog.repository.Tag2PostRepository;
import myBlog.repository.TagRepository;
import myBlog.repository.UserRepository;

@Service
public class PostService {

  private final RegistrationResponse registrationResponse = new RegistrationResponse();
  private final PostResponse postResponse = new PostResponse();

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  Tag2PostRepository tag2PostRepositoryRepository;

  public PostResponse getSearchedPosts(Pageable pageable, String query) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsBySearchQuery("%" + query + "%", pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    return postResponse;
  }

  public PostResponse getFilteredPosts(Pageable pageable, String mode) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsOrderByDateDesc(pageable);
    switch (mode) {
      case ("recent"):
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
        break;
      case ("early"):
        page = postRepository.findPostsOrderByDateAsc(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
        break;
      case ("best"):
        page = postRepository.findPostsOrderByLikes(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
        break;
      case ("popular"):
        page = postRepository.findPostsOrderByCommentCount(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mode);
    }
    return postResponse;
  }

  public PostResponse getPostsByDate(Pageable pageable, String date) {
    String dayStart = date + " 00:00:00";
    String dayEnd = date + " 23:59:59";
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsByDate(dayStart, dayEnd, pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    return postResponse;
  }

  public PostResponse getPostsByTag(Pageable pageable, String query) {
    String tag = query + "%";
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsByTag(tag, pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    return postResponse;
  }

  public PostIdDto getPostsById(int id) {
    return PostIdDto.of(postRepository.findById(id).get());
  }

  public PostResponse getModerationPosts(Pageable pageable, String status, String userEmail) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsForModeration(pageable);
    if (status.equals("new")) {
      postResponse.setCount((page.getTotalElements()));
      page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    } else {
      page = postRepository.findModeratedPosts(status, userRepository.findByEmail(userEmail).get().getId(), pageable);
      postResponse.setCount((page.getTotalElements()));
      page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post))
      );
    }
    return postResponse;
  }

  public PostResponse getMyPosts(Pageable pageable, String status, String userEmail) {
    int userId = userRepository.findByEmail(userEmail).get().getId();
    int isActive = 0;
    String moderationStatus = "%%";

    switch (status) {
      case "pending":
        isActive = 1;
        moderationStatus = "NEW";
        break;
      case "declined":
        isActive = 1;
        moderationStatus = "DECLINED";
        break;
      case "published":
        isActive = 1;
        moderationStatus = "ACCEPTED";
        break;
    }
    postResponse.setPosts(new ArrayList<>());

    Page<Post> page = postRepository.findMyPosts(userId, isActive, moderationStatus, pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    return postResponse;
  }

  public RegistrationResponse createPost(PostRequest postRequest, String userEmail) {
    boolean isActive = false;
    Long timestampNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    registrationResponse.setErrors(new HashMap<>());
    registrationResponse.setResult(false);

    if (postRequest.getTitle().length() < 3) {
      registrationResponse.getErrors().put("title", "Заголовок не установлен");
    }
    if (postRequest.getText().length() < 151) {
      registrationResponse.getErrors().put("text", "Текст публикации слишком короткий");
    }
    if (postRequest.getTimestamp() < timestampNow) {
      postRequest.setTimestamp(timestampNow);
    }
    LocalDateTime publicationTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(postRequest.getTimestamp()),
            TimeZone.getDefault().toZoneId());

    if (postRequest.getActive() == 1) {
      isActive = true;
    }

    if (registrationResponse.getErrors().size() == 0) {
      Post post = postRepository.save(Post.builder()
          .isActive(isActive)
          .moderationStatus(ModerationStatus.NEW)
          .publicationTime(publicationTime)
          .text(postRequest.getText())
          .title(postRequest.getTitle())
          .viewCount(0)
          .moderator(null)
          .user(userRepository.findByEmail(userEmail).get())
          .build());

      findTagOrCreate(postRequest.getTags()).forEach(tag -> {
        tag2PostRepositoryRepository.insertTag2Post(post.getId(), tag.getId());
      });

      registrationResponse.setResult(true);
      registrationResponse.setErrors(null);
    }
    return registrationResponse;
  }

  public RegistrationResponse updatePost(PostRequest postRequest, String userEmail, Integer postId) {
    boolean isActive = false;
    Long timestampNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    registrationResponse.setErrors(new HashMap<>());
    registrationResponse.setResult(false);

    postRepository.findById(postId).get().getTag2PostList().forEach(tag2Post -> {
      tag2PostRepositoryRepository.delete(tag2Post);
    });

    if (postRequest.getTitle().length() < 3) {
      registrationResponse.getErrors().put("title", "Заголовок не установлен");
    }
    if (postRequest.getText().length() < 151) {
      registrationResponse.getErrors().put("text", "Текст публикации слишком короткий");
    }
    if (postRequest.getTimestamp() < timestampNow) {
      postRequest.setTimestamp(timestampNow);
    }
    LocalDateTime publicationTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(postRequest.getTimestamp()),
            TimeZone.getDefault().toZoneId());

    if (postRequest.getActive() == 1) {
      isActive = true;
    }

    if (registrationResponse.getErrors().size() == 0) {
      Post post = postRepository.save(Post.builder()
          .id(postId)
          .isActive(isActive)
          .moderationStatus(ModerationStatus.NEW)
          .publicationTime(publicationTime)
          .text(postRequest.getText())
          .title(postRequest.getTitle())
          .viewCount(postRepository.findById(postId).get().getViewCount())
          .moderator(null)
          .user(userRepository.findByEmail(userEmail).get())
          .build());
      registrationResponse.setResult(true);
      registrationResponse.setErrors(null);

      findTagOrCreate(postRequest.getTags()).forEach(tag -> {
        tag2PostRepositoryRepository.insertTag2Post(post.getId(), tag.getId());
      });

    }
    return registrationResponse;
  }

  public RegistrationResponse moderatePost(ModerationRequest moderationRequest, String userEmail) {
    Post post = postRepository.findById(moderationRequest.getPostId()).get();
    User user = userRepository.findByEmail(userEmail).get();
    if (moderationRequest.getDecision().equals("accept")) {
      post.setModerationStatus(ModerationStatus.ACCEPTED);
    } else {
      post.setModerationStatus(ModerationStatus.DECLINED);
    }
    post.setModerator(user);
    postRepository.save(post);
    registrationResponse.setResult(true);
    return registrationResponse;
  }

  private List<Tag> findTagOrCreate(List<String> tags) {
    List<Tag> tagList = new ArrayList<>();
    tags.forEach(s -> {
      Optional<Tag> tag = tagRepository.findByName(s);
      if (tag.isPresent()) {
        tagList.add(tag.get());
      } else {
        tagList.add(tagRepository.save(Tag.builder()
            .name(s)
            .build()));
      }
    });
    return tagList;
  }
}
