package myBlog.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
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
      + "WHERE is_active = 1 AND moderation_status = 'ACCEPTED' "
      + "AND publication_time <= localtime()"
      + "GROUP BY  year "
      + "ORDER BY  year asc",
      nativeQuery = true)
  List<String> calendarYears();


  @Query(value = "SELECT date(publication_time),\n"
      + "count(*)\n"
      + "FROM posts\n"
      + "WHERE is_active = 1\n"
      + "AND moderation_status = 'ACCEPTED'\n"
      + "AND publication_time <= localtime()\n"
      + "AND publication_time\n"
      + "BETWEEN ?1\n"
      + "AND ?2\n"
      + "GROUP BY  date(publication_time) ",
      nativeQuery = true)
  List<String> findPostsByYear(String yearStart, String yearEnd);


  @Query(value = "SELECT *\n"
      + "FROM posts\n"
      + "WHERE publication_time BETWEEN ?1 AND ?2\n"
      + "AND is_active = 1 AND moderation_status = 'ACCEPTED' "
      + "AND publication_time <= localtime()"
      + "GROUP BY id",
      nativeQuery = true)
  Page<Post> findPostsByDate(String dayStart, String dayEnd, Pageable pageable);


  @Query(value = "SELECT * FROM posts "
      + "WHERE concat(text,title) LIKE ?1 "
      + "AND is_active = 1 AND moderation_status = 'ACCEPTED' "
      + "AND publication_time <= localtime()",
      nativeQuery = true)
  Page<Post> findPostsBySearchQuery(String query, Pageable pageable);


  @Query(value = "SELECT * FROM posts "
      + "WHERE is_active = 1     AND moderation_status = 'ACCEPTED'   "
      + "AND publication_time <= localtime()   "
      + "ORDER BY publication_time DESC",
      nativeQuery = true)
  Page<Post> findPostsOrderByDateDesc(Pageable pageable);

  @Query(value = "SELECT * FROM posts "
      + "WHERE is_active = 1     AND moderation_status = 'ACCEPTED'   "
      + "AND publication_time <= localtime()   "
      + "ORDER BY publication_time ASC",
      nativeQuery = true)
  Page<Post> findPostsOrderByDateAsc(Pageable pageable);


  @Query(value = "SELECT * FROM posts "
      + "LEFT JOIN users ON users.id = posts.user_id "
      + "LEFT JOIN post_comments  ON post_comments.post_id = posts.id    "
      + "LEFT JOIN post_votes  ON post_votes.post_id = posts.id AND post_votes.value = 1    "
      + "WHERE is_active = 1     AND moderation_status = 'ACCEPTED'   "
      + "AND publication_time <= localtime()   "
      + "GROUP BY posts.id "
      + "ORDER BY count(post_votes.value) DESC",
      nativeQuery = true)
  Page<Post> findPostsOrderByLikes(Pageable pageable);

  @Query(value = "SELECT * FROM posts "
      + "LEFT JOIN users ON users.id = posts.user_id "
      + "LEFT JOIN post_comments  ON post_comments.post_id = posts.id    "
      + "LEFT JOIN post_votes  ON post_votes.post_id = posts.id AND post_votes.value = 1    "
      + "WHERE is_active = 1 AND moderation_status = 'ACCEPTED'   "
      + "AND publication_time <= localtime()   "
      + "GROUP BY posts.id "
      + "ORDER BY count(post_comments.id) DESC",
      nativeQuery = true)
  Page<Post> findPostsOrderByCommentCount(Pageable pageable);

  @Query(value = "SELECT *\n"
      + "FROM posts\n"
      + "LEFT JOIN tag2post ON tag2post.post_id = posts.id\n"
      + "LEFT JOIN tags ON tags.id = tag2post.tag_id\n"
      + "WHERE is_active = 1\n"
      + "  AND moderation_status = 'ACCEPTED'\n"
      + "  AND publication_time <= localtime()\n"
      + "  AND tags.name like ?1",
      nativeQuery = true)
  Page<Post> findPostsByTag(String tag, Pageable pageable);


  @Query(value = "SELECT * FROM posts where moderation_status = 'NEW' AND is_active = 1 AND moderator_id IS null",
      nativeQuery = true)
  Page<Post> findPostsForModeration(Pageable pageable);


  @Query(value = "SELECT * FROM posts where moderation_status = ?1 AND is_active = 1 AND moderator_id = ?2",
      nativeQuery = true)
  Page<Post> findModeratedPosts(String status, Integer userId, Pageable pageable);

  @Query(value = "SELECT * FROM posts WHERE user_id = ?1 AND is_active = ?2 AND moderation_status like ?3",
      nativeQuery = true)
  Page<Post> findMyPosts(Integer userId, Integer isActive, String moderationStatus, Pageable pageable);


  @Query(value = "SELECT count(user_id) FROM posts where user_id = ?1 group by user_id",
      nativeQuery = true)
  Integer getMyPosts(Integer userId);

  @Query(value = "SELECT sum(view_count) FROM posts where user_id = ?1",
      nativeQuery = true)
  Integer getMyTotalViewCount(Integer userId);

  @Query(value = "SELECT sum(view_count) FROM posts",
      nativeQuery = true)
  Integer getTotalViewCount();


  @Query(value = "SELECT min(publication_time) FROM posts where user_id = ?1",
      nativeQuery = true)
  LocalDateTime getMyFirstPublication(Integer userId);

  @Query(value = "SELECT min(publication_time) FROM posts",
      nativeQuery = true)
  LocalDateTime getFirstPublication();

}