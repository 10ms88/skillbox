package myBlog.service;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.dto.GlobalSettingsDto;
import myBlog.enumuration.SettingCode;
import myBlog.model.GlobalSetting;
import myBlog.repository.GlobalSettingsRepository;

@Service
public class GlobalSettingsService {


  @Autowired
  GlobalSettingsRepository globalSettingsRepository;

  public GlobalSettingsDto getGlobalSettings() {
    List<GlobalSetting> globalSettingList = Lists.newArrayList(globalSettingsRepository.findAll());
    boolean MULTIUSER_MODE = true;
    boolean POST_PREMODERATION = true;
    boolean STATISTICS_IS_PUBLIC = true;
    for (GlobalSetting globalSetting : globalSettingList) {
      if (globalSetting.getCode().equals(SettingCode.MULTIUSER_MODE.toString()) && globalSetting.getValue().equals("NO")) {
        MULTIUSER_MODE = false;
      }
      if (globalSetting.getCode().equals(SettingCode.POST_PREMODERATION.toString()) && globalSetting.getValue().equals("NO")) {
        POST_PREMODERATION = false;
      }
      if (globalSetting.getCode().equals(SettingCode.STATISTICS_IS_PUBLIC.toString()) && globalSetting.getValue().equals("NO")) {
        STATISTICS_IS_PUBLIC = false;
      }
    }
    return new GlobalSettingsDto(MULTIUSER_MODE, POST_PREMODERATION, STATISTICS_IS_PUBLIC);
  }


  public void setGlobalSettings(GlobalSettingsDto globalSettingsDto) {
    List<GlobalSetting> globalSettingList = Lists.newArrayList(globalSettingsRepository.findAll());
    globalSettingList.forEach(setting -> {
      if (setting.getCode().equals(SettingCode.MULTIUSER_MODE.toString())) {
        if (globalSettingsDto.isMULTIUSER_MODE()) {
          setting.setValue("YES");
        } else {
          setting.setValue("NO");
        }
        globalSettingsRepository.save(setting);
      }
      if (setting.getCode().equals(SettingCode.STATISTICS_IS_PUBLIC.toString())) {
        if (globalSettingsDto.isSTATISTICS_IS_PUBLIC()) {
          setting.setValue("YES");
        } else {
          setting.setValue("NO");
        }
        globalSettingsRepository.save(setting);
      }
      if (setting.getCode().equals(SettingCode.POST_PREMODERATION.toString())) {
        if (globalSettingsDto.isPOST_PREMODERATION()) {
          setting.setValue("YES");
        } else {
          setting.setValue("NO");
        }
        globalSettingsRepository.save(setting);
      }
    });
  }
}

