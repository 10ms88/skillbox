package myBlog.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myBlog.api.request.LoginRequest;
import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.LoginResponse;
import myBlog.api.response.RegistrationResponse;
import myBlog.api.response.UserLoginResponse;
import myBlog.model.CaptchaCode;
import myBlog.model.User;
import myBlog.repository.CaptchaCodeRepository;
import myBlog.repository.UserRepository;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {


  private final RegistrationResponse registrationResponse = new RegistrationResponse();
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CaptchaCodeRepository captchaCodeRepository;


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


  public LoginResponse getLoginResponseFromEmail(String email) {
    myBlog.model.User currentUser =
        userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
    UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setEmail(currentUser.getEmail());
    userLoginResponse.setModeration(currentUser.getIsModerator());
    userLoginResponse.setName(currentUser.getName());
    userLoginResponse.setId(currentUser.getId());
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUserLoginResponse(userLoginResponse);
    return loginResponse;
  }

  public LoginResponse getLoginResponseFromLoginRequest(LoginRequest loginRequest) {
    Authentication auth = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

    return getLoginResponseFromEmail(user.getUsername());
  }
}
