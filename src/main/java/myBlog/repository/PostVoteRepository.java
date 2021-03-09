package myBlog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.PostVote;
import myBlog.model.User;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {

  @Query(value = "SELECT count(user_id) FROM post_votes where user_id = ?1 and value = 1 group by user_id",
      nativeQuery = true)
  Integer getMyLikeCount(int id);


  @Query(value = "SELECT count(user_id) FROM post_votes where user_id = ?1 and value = 0 group by user_id",
      nativeQuery = true)
  Integer getMyDislikeCount(int id);

  @Query(value = "SELECT count(value) FROM post_votes where value = 1 group by value",
      nativeQuery = true)
  Integer getLikeCount();


  @Query(value = "SELECT count(value) FROM post_votes where value = 0 group by value",
      nativeQuery = true)
  Integer getDislikeCount();

  @Query(value = "SELECT * FROM post_votes where post_id = ?1 and user_id = ?2",
      nativeQuery = true)
  Optional <PostVote> findPostVote(int postId, User user);
}
