package myBlog.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "post")
    private List<Tag2Post> postList;
}
