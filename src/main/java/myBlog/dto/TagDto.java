package myBlog.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDto {

  private final String name;
  private final Double weight;

  public static TagDto of(String name, Double weight) {
    return TagDto.builder()
        .name(name)
        .weight(weight)
        .build();
  }


}
