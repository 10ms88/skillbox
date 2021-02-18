package myBlog.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import myBlog.api.response.PostResponse;
import myBlog.dto.PostDto;
import myBlog.dto.PostIdDto;
import myBlog.model.Post;
import myBlog.repository.PostRepository;

@Service
public class PostService {

  private final PostResponse postResponse = new PostResponse();

  @Autowired
  PostRepository postRepository;

  public PostResponse getSearchedPosts(Pageable pageable, String query) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsBySearchQuery("%" + query + "%", pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> {
      postResponse.getPosts().add(PostDto.of(post));
    });
    return postResponse;
  }

  public PostResponse getFilteredPosts(Pageable pageable, String mode) {
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsOrderByDateDesc(pageable);
    switch (mode) {
      case ("recent"):
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> {
          postResponse.getPosts().add(PostDto.of(post));
        });
        break;
      case ("early"):
        page = postRepository.findPostsOrderByDateAsc(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> {
          postResponse.getPosts().add(PostDto.of(post));
        });
        break;
      case ("best"):
        page = postRepository.findPostsOrderByLikes(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> {
          postResponse.getPosts().add(PostDto.of(post));
        });
        break;
      case ("popular"):
        page = postRepository.findPostsOrderByCommentCount(pageable);
        postResponse.setCount((page.getTotalElements()));
        page.getContent().forEach(post -> {
          postResponse.getPosts().add(PostDto.of(post));
        });
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
    page.getContent().forEach(post -> {
      postResponse.getPosts().add(PostDto.of(post));
    });
    return postResponse;
  }

  public PostResponse getPostsByTag(Pageable pageable, String query) {
    String tag = query + "%";
    postResponse.setPosts(new ArrayList<>());
    Page<Post> page = postRepository.findPostsByTag(tag, pageable);
    postResponse.setCount((page.getTotalElements()));
    page.getContent().forEach(post -> {
      postResponse.getPosts().add(PostDto.of(post));
    });
    return postResponse;
  }

  public PostIdDto getPostsById(int id) {
    return PostIdDto.of(postRepository.findById(id).get());
  }
}
