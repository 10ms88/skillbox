package myBlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import myBlog.api.response.PostResponse;
import myBlog.dto.PostDto;
import myBlog.dto.PostIdDto;
import myBlog.model.Post;
import myBlog.repository.PostRepository;

@Slf4j
@Service
public class PostService {

  private final PostResponse postResponse = new PostResponse();

  @Autowired
  PostRepository postRepository;

  public PostResponse getSearchedPosts(Pageable pageable, String query) {

    log.info(postRepository.findPostsBySearchQuery("%" + query + "%", pageable).getPageable().toString());

    postResponse.setPosts(postRepository.findPostsBySearchQuery("%" + query + "%", pageable)
        .get()
        .map(PostDto::of)
        .collect(Collectors.toList()));
    return postResponse;
  }

  public PostResponse getFilteredPosts(Pageable pageable, String mode) {
    List<PostDto> postDtoList = new ArrayList<>();

    switch (mode) {
      case ("recent"):
        for (Post post : postRepository.findAll(Sort.by("publicationTime").descending())) {
          postDtoList.add(PostDto.of(post));
        }
        break;
      case ("early"):
        for (Post post : postRepository.findAll(Sort.by("publicationTime").ascending())) {
          postDtoList.add(PostDto.of(post));
        }
        break;
      case ("best"):
        postDtoList = postRepository.findPostsOrderByLikes(pageable)
            .get()
            .map(PostDto::of)
            .collect(Collectors.toList());
        break;
      case ("popular"):
        postDtoList = postRepository.findPostsOrderByCommentCount(pageable)
            .get()
            .map(PostDto::of)
            .collect(Collectors.toList());
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mode);
    }
    postResponse.setCount(postDtoList.size());
    postResponse.setPosts(postDtoList);
    return postResponse;
  }

  public PostResponse getPostsByDate(Pageable pageable, String date) {
    String dayStart = date + " 00:00:00";
    String dayEnd = date + " 23:59:59";
    postResponse.setPosts(postRepository.findPostsByDate(dayStart, dayEnd, pageable)
        .get()
        .map(PostDto::of)
        .collect(Collectors.toList()));
    return postResponse;
  }

  public PostResponse getPostsByTag(Pageable pageable, String query) {
    String tag = query + "%";
    postResponse.setPosts(postRepository.findPostsByTag(tag, pageable)
        .get()
        .map(PostDto::of)
        .collect(Collectors.toList()));
    return postResponse;
  }

  public PostIdDto getPostsById(int id) {
    return PostIdDto.of(postRepository.findById(id).get());
  }

}
