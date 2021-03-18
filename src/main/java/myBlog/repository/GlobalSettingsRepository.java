package myBlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.GlobalSetting;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {

}
