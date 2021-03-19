package myBlog.dto;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

import org.jsoup.Jsoup;

import myBlog.model.Post;
import myBlog.model.PostVote;

@Data
@Builder
public class PostDto {

  private final int id;
  private final Long timestamp;
  private final UserDto user;
  private final String title;
  private final String announce;
  private final int likeCount;
  private final int dislikeCount;
  private final int commentCount;
  private final int viewCount;

  public static PostDto of(Post post) {
    return PostDto.builder()
        .id(post.getId())
        .timestamp(ZonedDateTime.of(post.getPublicationTime(), ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
        .user(UserDto.of(post.getUser()))
        .title(post.getTitle())
        .announce(Jsoup.parse(post.getText()).text().substring(0, 150) + "...")
        .likeCount(getLikeList(post).size())
        .dislikeCount(getDisLikeList(post).size())
        .commentCount(post.getPostCommentList().size())
        .viewCount(post.getViewCount())
        .build();
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
