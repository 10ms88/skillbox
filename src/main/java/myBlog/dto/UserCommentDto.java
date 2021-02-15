package myBlog.dto;

import lombok.Builder;
import lombok.Data;

import myBlog.model.User;

@Data
@Builder
public class UserCommentDto {

  private final int id;
  private final String name;
  private final String photo;

  public static UserCommentDto of(User user) {
    return UserCommentDto.builder()
        .id(user.getId())
        .name(user.getName())
        .photo(user.getPhoto())
        .build();
  }


}
