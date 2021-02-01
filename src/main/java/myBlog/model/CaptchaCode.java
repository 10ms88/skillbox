package myBlog.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "captcha_codes")
@Data
public class CaptchaCode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDateTime generationTime;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String secretCode;


}
