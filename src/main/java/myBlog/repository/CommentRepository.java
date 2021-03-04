package myBlog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import myBlog.model.PostComment;

public interface CommentRepository extends CrudRepository<PostComment, Integer> {

  @Transactional
  @Modifying
  @Query(value = "UPDATE post_comments SET post_id = ?1, user_id = ?2 WHERE id = ?3", nativeQuery = true)
  void update(int postId, int userId, int commentId);
}
