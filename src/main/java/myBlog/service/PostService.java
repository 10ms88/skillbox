package myBlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    List<Post> foundPosts = postRepository.findAllBySearchQuery(query, pageable);
    postResponse.setCount(foundPosts.size());
    List<PostDto> postDtoList = new ArrayList<>();

    foundPosts.forEach(post -> {
      postDtoList.add(PostDto.of(post));
    });
    postResponse.setPosts(postDtoList);
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
        List<String> postsSortedByLikeCount = postRepository.sortByLikeCount(pageable);
        postDtoList = postsSortedByLikeCount
            .stream()
            .map(p -> p.split(","))
            .map(split -> PostDto.of(postRepository.findById(Integer.valueOf(split[0])).get()))
            .collect(Collectors.toList());
        break;
      case ("popular"):
        List<String> postsSortedByCommentCount = postRepository.sortByCommentCount(pageable);
        postDtoList = postsSortedByCommentCount
            .stream()
            .map(p -> p.split(","))
            .map(split -> PostDto.of(postRepository.findById(Integer.valueOf(split[0])).get()))
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
    postResponse.setPosts(new ArrayList<>());

    postRepository.getPostsByDate(dayStart, dayEnd, pageable).forEach(p -> {
      postResponse.getPosts().add(PostDto.of(p));
    });
    postResponse.setCount(postResponse.getPosts().size());
    return postResponse;
  }

  public PostResponse getPostsByTag(Pageable pageable, String query) {
    String tag = query + "%";
    postResponse.setPosts(new ArrayList<>());

    postRepository.getPostsByTag(tag, pageable).forEach(p -> {
      postResponse.getPosts().add(PostDto.of(postRepository.findById(p).get()));
    });
    postResponse.setCount(postResponse.getPosts().size());
    return postResponse;
  }

  public PostIdDto getPostsById(int id) {

    return PostIdDto.of(postRepository.findById(id).get());
  }

}
