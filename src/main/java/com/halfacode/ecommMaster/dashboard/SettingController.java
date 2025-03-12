package com.halfacode.ecommMaster.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping
    public ResponseEntity<List<Setting>> getAllSettings() {
        List<Setting> settings = settingService.listAllSettings();
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/general")
    public ResponseEntity<GeneralSettingBag> getGeneralSettings() {
        GeneralSettingBag generalSettings = settingService.getGeneralSettings();
        return ResponseEntity.ok(generalSettings);
    }

    @GetMapping("/mail-server")
    public ResponseEntity<List<Setting>> getMailServerSettings() {
        List<Setting> settings = settingService.getMailServerSettings();
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/mail-template")
    public ResponseEntity<List<Setting>> getMailTemplateSettings() {
        List<Setting> settings = settingService.getMailTemplateSettings();
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/currency")
    public ResponseEntity<List<Setting>> getCurrencySettings() {
        List<Setting> settings = settingService.getCurrencySettings();
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/payment")
    public ResponseEntity<List<Setting>> getPaymentSettings() {
        List<Setting> settings = settingService.getPaymentSettings();
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAllSettings(@RequestBody List<Setting> settings) {
        settingService.saveAll(settings);
        return ResponseEntity.ok("Settings saved successfully.");
    }
}
