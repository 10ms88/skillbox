package myBlog.service;

import com.google.common.collect.Iterables;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import myBlog.api.request.LoginRequest;
import myBlog.api.request.RegistrationRequest;
import myBlog.api.response.LoginResponse;
import myBlog.api.response.MainResponse;
import myBlog.api.response.UserLoginResponse;
import myBlog.api.response.StatisticResponse;
import myBlog.model.CaptchaCode;
import myBlog.model.User;
import myBlog.repository.CaptchaCodeRepository;
import myBlog.repository.PostRepository;
import myBlog.repository.PostVoteRepository;
import myBlog.repository.UserRepository;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {


  private final MainResponse mainResponse = new MainResponse();
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CaptchaCodeRepository captchaCodeRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostVoteRepository postVoteRepository;
  @Autowired
  private GlobalSettingsService globalSettingsService;


  public MainResponse createUser(RegistrationRequest registrationRequest) {
    mainResponse.setErrors(new HashMap<>());

    if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
      mainResponse.getErrors().put("email", "Этот e-mail уже зарегистрирован");
    }
    if (registrationRequest.getPassword().length() < 6) {
      mainResponse.getErrors().put("password", "Пароль короче 6-ти символов");
    }

    Pattern namePattern = Pattern.compile("^[a-zA-Z]+[a-zA-Z_0-9]*[a-zA-Z0-9]+$");
    Matcher nameMatcher = namePattern.matcher(registrationRequest.getName());
    if (!nameMatcher.matches()) {
      mainResponse.getErrors().put("name", "Имя указано неверно");
    }

    Optional<CaptchaCode> captchaCode = captchaCodeRepository.findByCode(registrationRequest.getCaptcha());
    if (captchaCode.isEmpty()) {
      mainResponse.getErrors().put("captcha", "Код с картинки введён неверно");
    }

    if (mainResponse.getErrors().size() == 0) {
      userRepository.save(User.builder()
          .isModerator(false)
          .email(registrationRequest.getEmail())
          .registrationTime(LocalDateTime.now())
          .name(registrationRequest.getName())
          .password(BCrypt.hashpw(registrationRequest.getPassword(), BCrypt.gensalt()))
          .code(null)
          .photo(null)
          .build());
      mainResponse.setResult(true);
      mainResponse.setErrors(null);
    }
    return mainResponse;
  }


  public LoginResponse getLoginResponseFromEmail(String email) {

    myBlog.model.User currentUser =
        userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));

    UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setId(currentUser.getId());
    userLoginResponse.setName(currentUser.getName());
    userLoginResponse.setPhoto(currentUser.getPhoto());
    userLoginResponse.setEmail(currentUser.getEmail());
    userLoginResponse.setModeration(currentUser.getIsModerator());
    if (!currentUser.getIsModerator()) {
      userLoginResponse.setModerationCount(0);
      userLoginResponse.setSettings(false);
    } else {
      userLoginResponse.setModerationCount((int) postRepository.findPostsForModeration(PageRequest.of(0, 10)).getTotalElements());
      userLoginResponse.setSettings(true);
    }

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUserLoginResponse(userLoginResponse);
    return loginResponse;
  }

  public LoginResponse setAuthentication(LoginRequest loginRequest) {
    Authentication auth = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

    return getLoginResponseFromEmail(user.getUsername());
  }

  public StatisticResponse getMyStatistic(String userEmail) {
    User user = userRepository.findByEmail(userEmail).get();

    return StatisticResponse.builder()
        .postsCount(postRepository.getMyPosts(user.getId()))
        .likesCount(postVoteRepository.getMyLikeCount(user.getId()))
        .dislikesCount(postVoteRepository.getMyDislikeCount(user.getId()))
        .viewsCount(postRepository.getMyTotalViewCount(user.getId()))
        .firstPublication(ZonedDateTime.of(postRepository.getMyFirstPublication(user.getId()), ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
        .build();
  }

  public StatisticResponse getAllStatistic() {
    if (globalSettingsService.getGlobalSettings().isSTATISTICS_IS_PUBLIC()) {
      return StatisticResponse.builder()
          .postsCount(Iterables.size(postRepository.findAll()))
          .likesCount(postVoteRepository.getLikeCount())
          .dislikesCount(postVoteRepository.getDislikeCount())
          .viewsCount(postRepository.getTotalViewCount())
          .firstPublication(ZonedDateTime.of(postRepository.getFirstPublication(), ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
          .build();
    } else {
      return null;
    }
  }
}
