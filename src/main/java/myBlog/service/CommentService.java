package myBlog.service;


import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.api.request.CommentRequest;
import myBlog.api.response.CommentResponse;
import myBlog.exeption.CommentException;
import myBlog.model.Post;
import myBlog.model.PostComment;
import myBlog.model.User;
import myBlog.repository.CommentRepository;
import myBlog.repository.PostRepository;
import myBlog.repository.UserRepository;

@Service
public class CommentService {


  private final CommentResponse commentResponse = new CommentResponse();
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserRepository userRepository;

  public CommentResponse addComment(CommentRequest commentRequest, Integer userId) {
    Post post = postRepository.findById(commentRequest.getPostId()).get();
    User user = userRepository.findById(userId).get();
    if (commentRequest.getText().length() > 5) {
       commentRepository.save(PostComment.builder()
          .post(post)
          .user(user)
          .parentId(commentRequest.getParentId())
          .commentTime(LocalDateTime.now())
          .text(commentRequest.getText())
          .build());
      return commentResponse;
    } else {
      throw new CommentException();
    }
  }
}
