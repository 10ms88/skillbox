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
import org.hibernate.annotations.Type;

@Builder
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {

  public User() {
  }


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean isModerator;

  @Column(nullable = false)
  private LocalDateTime registrationTime;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private String code;

  @Type(type = "text")
  private String photo;

}
