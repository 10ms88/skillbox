package myBlog.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({PARAMETER, TYPE})
@Retention(RUNTIME)
@AuthenticationPrincipal(expression = "id")
public @interface UserId {

}
