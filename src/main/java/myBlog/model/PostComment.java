package myBlog.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_comments")
@Data
public class PostComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private Integer parentId;

    @ManyToOne
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime commentTime;

    @Column(nullable = false)
    @Type(type = "text")
    private String text;


}
