package myBlog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import myBlog.model.Tag2Post;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {


  @Transactional
  @Modifying
  @Query(value = "INSERT INTO tag2post (post_id, tag_id) VALUES (?1, ?2)", nativeQuery = true)
  void insertTag2Post(int postId, int tagId);
}
