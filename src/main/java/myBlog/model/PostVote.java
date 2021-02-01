package myBlog.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    LocalDateTime voteTime;

    @Column(nullable = false)
    @Type(type = "byte")
    Boolean value;


}
