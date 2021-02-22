package myBlog.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.RegistrationResponse;
import myBlog.model.CaptchaCode;
import myBlog.model.User;
import myBlog.repository.CaptchaCodeRepository;
import myBlog.repository.UserRepository;

@Service
public class UserService {

  private final RegistrationResponse registrationResponse = new RegistrationResponse();


  @Autowired
  UserRepository userRepository;

  @Autowired
  CaptchaCodeRepository captchaCodeRepository;


  public RegistrationResponse createUser(RegistrationRequest registrationRequest) {
    registrationResponse.setErrors(new HashMap<>());

    if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
      registrationResponse.getErrors().put("email", "Этот e-mail уже зарегистрирован");
    }
    if (registrationRequest.getPassword().length() < 6) {
      registrationResponse.getErrors().put("password", "Пароль короче 6-ти символов");
    }

    Pattern namePattern = Pattern.compile("^[a-zA-Z]+[a-zA-Z_0-9]*[a-zA-Z0-9]+$");
    Matcher nameMatcher = namePattern.matcher(registrationRequest.getName());
    if (!nameMatcher.matches()) {
      registrationResponse.getErrors().put("name", "Имя указано неверно");
    }

    Optional<CaptchaCode> captchaCode = captchaCodeRepository.findByCode(registrationRequest.getCaptcha());
    if (captchaCode.isEmpty()) {
      registrationResponse.getErrors().put("captcha", "Код с картинки введён неверно");
    } else {
      userRepository.save(User.builder()
          .isModerator(false)
          .email(registrationRequest.getEmail())
          .registrationTime(LocalDateTime.now())
          .name(registrationRequest.getName())
          .password(registrationRequest.getPassword())
          .code(null)
          .photo(null)
          .build());

      registrationResponse.setResult(true);
      registrationResponse.setErrors(null);

    }

    return registrationResponse;
  }


}
