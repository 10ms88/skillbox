package myBlog.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Entity
@Table(name = "captcha_codes")
@Data
@Builder
@AllArgsConstructor
public class CaptchaCode {

  public CaptchaCode() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private LocalDateTime generationTime;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String secretCode;

}
