package myBlog.dto;

import lombok.Builder;
import lombok.Data;

import myBlog.model.User;

@Data
@Builder
public class UserDto {

  private final int id;
  private final String name;

  public static UserDto of(User user) {
    return UserDto.builder()
        .id(user.getId())
        .name(user.getName())
        .build();
  }
}
