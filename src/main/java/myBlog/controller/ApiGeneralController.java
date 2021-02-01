package myBlog.controller;

import myBlog.api.response.InitResponse;
import myBlog.api.response.SettingsResponse;
import myBlog.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/", produces = "application/json; charset=utf-8")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;

    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }


    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settingsResponse() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
    }


    @GetMapping("/init")
    private InitResponse init() {

        return initResponse;
    }

}
