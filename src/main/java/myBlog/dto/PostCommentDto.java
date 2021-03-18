package myBlog.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

import myBlog.model.PostComment;

@Data
@Builder
public class PostCommentDto {

  private final int id;
  private final Long timestamp;
  private final String text;
  private final UserCommentDto user;

  public static PostCommentDto of(PostComment postComment) {
    return PostCommentDto.builder()
        .id(postComment.getId())
        .timestamp(ZonedDateTime.of(postComment.getCommentTime(), ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
        .text(postComment.getText())
        .user(UserCommentDto.of(postComment.getUser()))
        .build();
  }


  private static List<UserCommentDto> getUserList(PostComment postComment) {
    List<UserCommentDto> userCommentDtoList = new ArrayList<>();
    postComment.getPost().getPostCommentList().forEach(comment -> {
      userCommentDtoList.add(UserCommentDto.of(comment.getUser()));
    });
    return userCommentDtoList;
  }
}


