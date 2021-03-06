package myBlog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

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


}
