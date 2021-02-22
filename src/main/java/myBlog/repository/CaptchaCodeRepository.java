package myBlog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import myBlog.model.CaptchaCode;

public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

  @Transactional
  @Modifying
  @Query(value = "delete from captcha_codes where  14400 < unix_timestamp(NOW())-unix_timestamp(generation_time) and id > 0", nativeQuery = true)
  void deleteOldCaptchaCode();


  @Query(value = "SELECT * FROM captcha_codes where code = ?1", nativeQuery = true)
  Optional<CaptchaCode> findByCode(String code);

}
