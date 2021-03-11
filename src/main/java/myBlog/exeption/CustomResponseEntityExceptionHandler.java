package myBlog.exeption;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(FileTypeException.class)
  protected ResponseEntity<NewException> handleFileTypeException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("image", "Загрузите изоображение в формате .jpg или .png");

    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = MultipartException.class)
  protected ResponseEntity<NewException> handleFileSizeLimitExceededException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("image", "Размер файла превышает допустимый размер");
    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ExistEmailException.class)
  protected ResponseEntity<NewException> handleExistEmailException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("email", "Этот e-mail уже зарегистрирован");
    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = CommentException.class)
  protected ResponseEntity<NewException> handleCommentException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("text", "Текст комментария не задан или слишком короткий");
    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = SpelEvaluationException.class)
  protected ResponseEntity<NewException> handleSpelEvaluationException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("text", "Пользователь не авторизован");
    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = PasswordLengthException.class)
  protected ResponseEntity<NewException> handlePasswordLengthException() {
    HashMap<String, String> errors = new HashMap<>();
    errors.put("password", "Пароль короче 6-ти символов");
    return new ResponseEntity<>(new NewException(false, errors), HttpStatus.BAD_REQUEST);
  }



  @Data
  @AllArgsConstructor
  private static class NewException {

    private Boolean result;
    private Map<String, String> errors;
  }

}
