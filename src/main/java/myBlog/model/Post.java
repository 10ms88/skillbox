package myBlog.model;


import lombok.Data;
import myBlog.enumuration.ModerationStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Type(type = "byte")
    private Boolean isActive;


    @Type(type = "string")
    private ModerationStatus moderationStatus;


    private Integer moderatorId;

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

    @OneToMany(mappedBy = "tag")
    private List<Tag2Post> tagList;

}