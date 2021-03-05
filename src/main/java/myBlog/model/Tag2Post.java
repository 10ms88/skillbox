package myBlog.model;


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

@Entity
@Table(name = "tag2post")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag2Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "post_id", updatable = false, insertable = false)
  private Post post;
  @ManyToOne
  @JoinColumn(name = "tag_id", updatable = false, insertable = false)
  private Tag tag;
}
