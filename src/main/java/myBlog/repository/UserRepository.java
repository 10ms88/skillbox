package myBlog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import myBlog.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query(value = "SELECT * FROM users where email = ?1",
      nativeQuery = true)
  Optional<User> findByEmail(String email);

  @Query(value = "SELECT * FROM users where code = ?1",
      nativeQuery = true)
  Optional<User> findByCode(String code);

}
