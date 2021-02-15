package myBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.GlobalSetting;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

}
