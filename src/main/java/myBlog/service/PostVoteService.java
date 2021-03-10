package myBlog.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import myBlog.api.request.PostVoteRequest;
import myBlog.api.response.MainResponse;
import myBlog.model.PostVote;
import myBlog.repository.CaptchaCodeRepository;
import myBlog.repository.PostRepository;
import myBlog.repository.PostVoteRepository;
import myBlog.repository.UserRepository;

@Service
public class PostVoteService {


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


  public MainResponse addLike(PostVoteRequest postVoteRequest, Integer userId) {
    Optional<PostVote> postVoteOptional = postVoteRepository.findPostVote(postVoteRequest.getPostId(), userRepository.findById(userId).get());
    if (postVoteOptional.isPresent()) {
      PostVote postVote = postVoteOptional.get();
      if (!postVote.getValue()) {
        postVote.setValue(true);
        postVoteRepository.save(postVote);
        mainResponse.setResult(true);
      } else {
        mainResponse.setResult(false);
      }
    } else {
      postVoteRepository.save(PostVote.builder()
          .value(true)
          .voteTime(LocalDateTime.now())
          .post(postRepository.findById(postVoteRequest.getPostId()).get())
          .user(userRepository.findById(userId).get())
          .build());
      mainResponse.setResult(true);
    }
    return mainResponse;
  }

  public MainResponse addDislike(PostVoteRequest postVoteRequest, Integer userId) {
    Optional<PostVote> postVoteOptional = postVoteRepository.findPostVote(postVoteRequest.getPostId(), userRepository.findById(userId).get());
    if (postVoteOptional.isPresent()) {
      PostVote postVote = postVoteOptional.get();
      if (postVote.getValue()) {
        postVote.setValue(false);
        postVoteRepository.save(postVote);
        mainResponse.setResult(true);
      } else {
        mainResponse.setResult(false);
      }
    } else {
      postVoteRepository.save(PostVote.builder()
          .value(false)
          .voteTime(LocalDateTime.now())
          .post(postRepository.findById(postVoteRequest.getPostId()).get())
          .user(userRepository.findById(userId).get())
          .build());
      mainResponse.setResult(true);
    }
    return mainResponse;
  }
}
