package myBlog.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.dto.GlobalSettingsDto;
import myBlog.model.GlobalSetting;
import myBlog.repository.GlobalSettingsRepository;

@Service
public class SettingsService {


  @Autowired
  GlobalSettingsRepository globalSettingsRepository;


  public GlobalSettingsDto getGlobalSettings() {

    List<GlobalSetting> globalSettingList = globalSettingsRepository.findAll();
    boolean MULTIUSER_MODE = true;
    boolean POST_PREMODERATION = true;
    boolean STATISTICS_IS_PUBLIC = true;
    for (GlobalSetting globalSettings : globalSettingList) {
      if (globalSettings.getCode().equals("MULTIUSER_MODE") && globalSettings.getValue().equals("NO")) {
        MULTIUSER_MODE = false;
      }
      if (globalSettings.getCode().equals("POST_PREMODERATION") && globalSettings.getValue().equals("NO")) {
        POST_PREMODERATION = false;
      }
      if (globalSettings.getCode().equals("STATISTICS_IS_PUBLIC") && globalSettings.getValue().equals("NO")) {
        STATISTICS_IS_PUBLIC = false;
      }
    }
    return new GlobalSettingsDto(MULTIUSER_MODE, POST_PREMODERATION, STATISTICS_IS_PUBLIC);
  }


}
