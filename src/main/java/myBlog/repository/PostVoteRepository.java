package myBlog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.PostVote;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Integer> {

  @Query(value = "  SELECT count(user_id) FROM post_votes where user_id = ?1 and value = 1 group by user_id\n",
      nativeQuery = true)
  Integer getLikeCount(int id);


  @Query(value = "  SELECT count(user_id) FROM post_votes where user_id = ?1 and value = 0 group by user_id\n",
      nativeQuery = true)
  Integer getDislikeCount(int id);

}
