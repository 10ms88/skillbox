package myBlog.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@Data
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
