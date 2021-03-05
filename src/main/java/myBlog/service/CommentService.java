package myBlog.service;


import java.time.LocalDateTime;
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

  public CommentResponse addComment(CommentRequest commentRequest, String userEmail) {
    Post post = postRepository.findById(commentRequest.getPostId()).get();
    User user = userRepository.findByEmail(userEmail).get();
    if (commentRequest.getText().length() > 5) {
      PostComment comment = commentRepository.save(PostComment.builder()
          .post(post)
          .user(user)
          .parentId(commentRequest.getParentId())
          .commentTime(LocalDateTime.now())
          .text(commentRequest.getText())
          .build());
      commentResponse.setCommentId(comment.getId());
      commentRepository.update(post.getId(), user.getId(), comment.getId());
      return commentResponse;
    } else {
      throw new CommentException();
    }
  }
}
