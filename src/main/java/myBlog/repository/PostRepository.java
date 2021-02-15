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


  @Query(value = "SELECT count(*) AS cnt,"
      + "date(publication_time) AS publication_time "
      + "FROM posts "
      + "WHERE publication_time "
      + "BETWEEN ?1 "
      + "AND ?2 "
      + "GROUP BY publication_time",
      nativeQuery = true)
  List<String> calendarPosts(String yearStart, String yearEnd);


  @Query(value = "SELECT * FROM posts WHERE concat(text,title) LIKE ?1",
      nativeQuery = true)
  List<Post> findAllBySearchQuery(String query, Pageable pageable);


  @Query(value = "SELECT post_id,\n"
      + "       sum(like_cnt) AS like_cnt\n"
      + "FROM\n"
      + "  (SELECT id AS post_id,\n"
      + "          COUNT(id) - 1 AS like_cnt\n"
      + "   FROM blogdb.posts\n"
      + "   GROUP BY id\n"
      + "   UNION ALL\n"
      + "     (SELECT post_id,\n"
      + "             count(value) AS like_cnt\n"
      + "      FROM post_votes\n"
      + "      WHERE value = 1\n"
      + "      GROUP BY post_id)) AS asd\n"
      + "GROUP BY post_id\n"
      + "ORDER BY like_cnt DESC",
      nativeQuery = true)
  List<String> sortByLikeCount(Pageable pageable);


  @Query(value = "SELECT post_id,\n"
      + "       sum(comment_cnt) AS comment_cnt\n"
      + "FROM\n"
      + "  (SELECT id AS post_id,\n"
      + "          COUNT(id) - 1 AS comment_cnt\n"
      + "   FROM blogdb.posts\n"
      + "   GROUP BY id\n"
      + "   UNION ALL\n"
      + "     (SELECT post_id,\n"
      + "             count(*) AS comment_cnt\n"
      + "      FROM post_comments\n"
      + "      GROUP BY post_id)) AS asd\n"
      + "GROUP BY post_id\n"
      + "ORDER BY comment_cnt DESC",
      nativeQuery = true)
  List<String> sortByCommentCount(Pageable pageable);


}