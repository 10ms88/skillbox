package myBlog.model;


import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post_comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostComment {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private Integer parentId;

  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private LocalDateTime commentTime;

  @Column(nullable = false)
  @Type(type = "text")
  private String text;


}
