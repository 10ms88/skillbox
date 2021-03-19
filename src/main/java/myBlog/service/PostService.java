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
import javax.lang.model.element.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import myBlog.api.request.ModerationRequest;
import myBlog.api.request.PostRequest;
import myBlog.api.response.MainResponse;
import myBlog.api.response.PostResponse;
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

  private final MainResponse mainResponse = new MainResponse();
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

  public PostIdDto getPostById(int id) {
    Post post = postRepository.findById(id).get();
    post.setViewCount(post.getViewCount() + 1);
    return PostIdDto.of(postRepository.save(post));
  }

  public PostResponse getModerationPosts(Pageable pageable, String status, Integer userId) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsForModeration(pageable);
    if (status.equals("new")) {
      postResponse.setCount((page.getTotalElements()));
      page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post)));
    } else {
      page = postRepository.findModeratedPosts(status, userRepository.findById(userId).get().getId(), pageable);
      postResponse.setCount((page.getTotalElements()));
      page.getContent().forEach(post -> postResponse.getPosts().add(PostDto.of(post))
      );
    }
    return postResponse;
  }

  public PostResponse getMyPosts(Pageable pageable, String status, Integer uId) {
    int userId = userRepository.findById(uId).get().getId();
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

  public MainResponse createPost(PostRequest postRequest, Integer userId) {
    boolean isActive = false;
    Long timestampNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    mainResponse.setErrors(new HashMap<>());
    mainResponse.setResult(false);

    if (postRequest.getTitle().length() < 3) {
      mainResponse.getErrors().put("title", "Заголовок не установлен");
    }
    if (Jsoup.parse(postRequest.getText()).text().length() < 151) {
      mainResponse.getErrors().put("text", "Текст публикации слишком короткий");
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


    if (mainResponse.getErrors().size() == 0) {
      Post post = postRepository.save(Post.builder()
          .isActive(isActive)
          .moderationStatus(ModerationStatus.NEW)
          .publicationTime(publicationTime)
          .text(postRequest.getText())
          .title(postRequest.getTitle())
          .viewCount(0)
          .moderator(null)
          .user(userRepository.findById(userId).get())
          .build());

      findTagOrCreate(postRequest.getTags()).forEach(tag -> {
        tag2PostRepositoryRepository.insertTag2Post(post.getId(), tag.getId());
      });

      mainResponse.setResult(true);
      mainResponse.setErrors(null);
    }
    return mainResponse;
  }

  public MainResponse updatePost(PostRequest postRequest, Integer userId, Integer postId) {
    boolean isActive = false;
    Long timestampNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    mainResponse.setErrors(new HashMap<>());
    mainResponse.setResult(false);

    postRepository.findById(postId).get().getTag2PostList().forEach(tag2Post -> {
      tag2PostRepositoryRepository.delete(tag2Post);
    });

    if (postRequest.getTitle().length() < 3) {
      mainResponse.getErrors().put("title", "Заголовок не установлен");
    }
    if (postRequest.getText().length() < 151) {
      mainResponse.getErrors().put("text", "Текст публикации слишком короткий");
    }
    if (postRequest.getTimestamp() < timestampNow) {
      postRequest.setTimestamp(timestampNow);
    }
    if (postRequest.getActive() == 1) {
      isActive = true;
    }

    if (mainResponse.getErrors().size() == 0) {
      Post post = postRepository.save(Post.builder()
          .id(postId)
          .isActive(isActive)
          .moderationStatus(ModerationStatus.NEW)
          .publicationTime(postRepository.findById(postId).get().getPublicationTime())
          .text(postRequest.getText())
          .title(postRequest.getTitle())
          .viewCount(postRepository.findById(postId).get().getViewCount())
          .moderator(null)
          .user(postRepository.findById(postId).get().getUser())
          .build());
      mainResponse.setResult(true);
      mainResponse.setErrors(null);

      findTagOrCreate(postRequest.getTags()).forEach(tag -> {
        tag2PostRepositoryRepository.insertTag2Post(post.getId(), tag.getId());
      });

    }
    return mainResponse;
  }

  public MainResponse moderatePost(ModerationRequest moderationRequest, Integer userId) {
    Post post = postRepository.findById(moderationRequest.getPostId()).get();
    User user = userRepository.findById(userId).get();
    if (moderationRequest.getDecision().equals("accept")) {
      post.setModerationStatus(ModerationStatus.ACCEPTED);
    } else {
      post.setModerationStatus(ModerationStatus.DECLINED);
    }
    post.setModerator(user);
    postRepository.save(post);
    mainResponse.setResult(true);
    return mainResponse;
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
