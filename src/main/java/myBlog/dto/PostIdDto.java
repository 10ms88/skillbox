package myBlog.dto;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

import myBlog.model.Post;
import myBlog.model.PostVote;

@Data
@Builder
public class PostIdDto {

  private final int id;
  private final Long timestamp;
  private final boolean active;
  private final UserDto user;
  private final String title;
  private final String text;
  private final int likeCount;
  private final int dislikeCount;
  private final int viewCount;
  private final List<PostCommentDto> comments;
  private final List<String> tags;

  public static PostIdDto of(Post post) {
    return PostIdDto.builder()
        .id(post.getId())
        .timestamp(ZonedDateTime.of(post.getPublicationTime(), ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
        .active(post.getIsActive())
        .user(UserDto.of(post.getUser()))
        .title(post.getTitle())
        .text(post.getText())
        .likeCount(getLikeList(post).size())
        .dislikeCount(getDisLikeList(post).size())
        .viewCount(post.getViewCount())
        .comments(getComments(post))
        .tags(getTags(post))
        .build();
  }


  private static List<String> getTags(Post post) {
    List<String> tagList = new ArrayList<>();

    post.getTag2PostList().forEach(tag2Post -> {
      tagList.add(tag2Post.getTag().getName());
    });

    return tagList;
  }

  private static List<PostCommentDto> getComments(Post post) {
    List<PostCommentDto> postCommentDtoList = new ArrayList<>();
    post.getPostCommentList().forEach(postComment -> {
      postCommentDtoList.add(PostCommentDto.of(postComment));
    });
    return postCommentDtoList;
  }

  private static List<PostVote> getLikeList(Post post) {
    List<PostVote> likeList = new ArrayList<>();
    for (int i = 0; i < post.getPostVoteList().size(); i++) {
      if (post.getPostVoteList().get(i).getValue()) {
        likeList.add(post.getPostVoteList().get(i));
      }
    }
    return likeList;
  }

  private static List<PostVote> getDisLikeList(Post post) {
    List<PostVote> disLikeList = new ArrayList<>();
    for (int i = 0; i < post.getPostVoteList().size(); i++) {
      if (!post.getPostVoteList().get(i).getValue()) {
        disLikeList.add(post.getPostVoteList().get(i));
      }
    }
    return disLikeList;
  }
}
