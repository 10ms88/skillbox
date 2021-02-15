package myBlog.model;


import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post_votes")
@Data
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;


  @OneToOne(cascade = CascadeType.ALL)
  private User user;

  @OneToOne(cascade = CascadeType.ALL)
  private Post post;

  @Column(nullable = false)
  private LocalDateTime voteTime;

  @Column(nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean value;


}
