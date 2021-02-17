package myBlog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import myBlog.model.GlobalSetting;

@Repository
public interface GlobalSettingsRepository extends PagingAndSortingRepository<GlobalSetting, Integer> {

}
