package myBlog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import myBlog.model.Tag;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

  @Query(value = "SELECT *\n"
      + "FROM tags\n"
      + "WHERE name LIKE ?1",
      nativeQuery = true)
  List<Tag> findAllByStartWith(String name);



  @Query(value = "SELECT * FROM tags WHERE name = ?1", nativeQuery = true)
  Optional<Tag> findByName(String name);

  @Transactional
  @Modifying
  @Query(value = "INSERT INTO tags (name) VALUES (?1)", nativeQuery = true)
  Tag insertTag(String name);

}
