package myBlog.model;


import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import myBlog.enumuration.ModerationStatus;


@Entity
@Table(name = "posts")
@Data
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean isActive;

  @Column(columnDefinition = "enum('NEW','ACCEPTED','DECLINED')")
  @Enumerated(EnumType.STRING)
  private ModerationStatus moderationStatus;

  @ManyToOne(cascade = CascadeType.ALL)
  private User moderator;

  @ManyToOne(cascade = CascadeType.ALL)
  private User user;

  @Column(nullable = false)
  private LocalDateTime publicationTime;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Type(type = "text")
  private String text;

  private int viewCount;

  @OneToMany(mappedBy = "post")
  private List<Tag2Post> tag2PostList;

  @OneToMany(mappedBy = "post")
  private List<PostComment> postCommentList;

  @OneToMany(mappedBy = "post")
  private List<PostVote> postVoteList;


}