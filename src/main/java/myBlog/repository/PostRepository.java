package myBlog.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {


  @Query(value = "SELECT EXTRACT(YEAR "
      + "FROM publication_time) AS year "
      + "FROM posts "
      + "GROUP BY  year "
      + "ORDER BY  year asc",
      nativeQuery = true)
  List<String> calendarYears();


  @Query(value = "SELECT date(publication_time),\n"
      + "       count(*)\n"
      + "FROM posts\n"
      + "WHERE is_active = 1\n"
      + "  AND moderation_status = 'ACCEPTED'\n"
      + "  AND publication_time BETWEEN ?1 AND ?2\n"
      + "GROUP BY publication_time",
      nativeQuery = true)
  List<String> calendarPosts(String yearStart, String yearEnd);

  @Query(value = "SELECT *\n"
      + "FROM posts\n"
      + "WHERE publication_time BETWEEN ?1 AND ?2\n"
      + "GROUP BY id",
      nativeQuery = true)
  List<Post> getPostsByDate(String dayStart, String dayEnd, Pageable pageable);


  @Query(value = "SELECT * FROM posts WHERE concat(text,title) LIKE ?1",
      nativeQuery = true)
  List<Post> findAllBySearchQuery(String query, Pageable pageable);


  @Query(value = "SELECT p.*\n"
      + "FROM posts p\n"
      + "LEFT JOIN users u ON u.id = p.user_id\n"
      + "LEFT JOIN post_comments pc ON pc.post_id = p.id\n"
      + "LEFT JOIN post_votes pvl ON pvl.post_id = p.id AND pvl.value = 1\n"
      + "WHERE p.is_active = 1\n"
      + "  AND p.moderation_status = 'ACCEPTED'\n"
      + "  AND p.publication_time <= current_date()\n"
      + "GROUP BY p.id\n"
      + "ORDER BY count(pvl.value) DESC",
      nativeQuery = true)
  List<String> findPostsOrderByLikes(Pageable pageable);


  @Query(value = "SELECT p.*\n"
      + "FROM posts p\n"
      + "LEFT JOIN users u ON u.id = p.user_id\n"
      + "LEFT JOIN post_comments pc ON pc.post_id = p.id\n"
      + "LEFT JOIN post_votes pvl ON pvl.post_id = p.id AND pvl.value = 1\n"
      + "WHERE p.is_active = 1\n"
      + "  AND p.moderation_status = 'ACCEPTED'\n"
      + "  AND p.publication_time <= current_date()\n"
      + "GROUP BY p.id\n"
      + "ORDER BY count(pc.id) DESC",
      nativeQuery = true)
  List<String> findPostsOrderByCommentCount(Pageable pageable);


  @Query(value = "SELECT post_id\n"
      + "FROM tag2post\n"
      + "JOIN tags ON tag2post.tag_id = tags.id\n"
      + "WHERE name like ?1\n"
      + "GROUP BY post_id",
      nativeQuery = true)
  List<Integer> getPostsByTag(String tag, Pageable pageable);
}