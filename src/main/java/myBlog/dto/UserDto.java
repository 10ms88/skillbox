package myBlog.dto;

import lombok.Builder;
import lombok.Data;

import myBlog.model.User;

@Data
@Builder
public class UserDto {

  private final int id;
  //  private Boolean isModerator;
//  private LocalDateTime registrationTime;
  private final String name;
//  private String email;
//  private String password;
//  private String code;
//  private String photo;

  public static UserDto of(User user) {
    return UserDto.builder()
        .id(user.getId())
//        .isModerator(user.getIsModerator())
//        .registrationTime(user.getRegistrationTime())
        .name(user.getName())
//        .email(user.getEmail())
//        .password(user.getPassword())
//        .code(user.getCode())
//        .photo((user.getPhoto()))
        .build();
  }


}
