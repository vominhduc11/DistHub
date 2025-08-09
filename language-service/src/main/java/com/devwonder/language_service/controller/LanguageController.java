package com.devwonder.language_service.controller;

import com.devwonder.language_service.model.Language;
import com.devwonder.language_service.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
@Validated
public class LanguageController {
    
    private final LanguageService languageService;
    
    // Get all supported languages
    @GetMapping("/supported")
    public ResponseEntity<List<Language>> getSupportedLanguages() {
        List<Language> languages = languageService.getSupportedLanguages();
        log.info("Retrieved {} supported languages", languages.size());
        return ResponseEntity.ok(languages);
    }
    
    // Get all languages (including disabled)
    @GetMapping("/all")
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        log.info("Retrieved {} total languages", languages.size());
        return ResponseEntity.ok(languages);
    }
    
    // Get language by code
    @GetMapping("/{code}")
    public ResponseEntity<Language> getLanguageByCode(@PathVariable String code) {
        Optional<Language> languageOpt = languageService.getLanguageByCode(code);
        if (languageOpt.isPresent()) {
            log.info("Retrieved language: {} ({})", languageOpt.get().getName(), code);
            return ResponseEntity.ok(languageOpt.get());
        } else {
            log.warn("Language not found: {}", code);
            return ResponseEntity.notFound().build();
        }
    }
    
    // Create new language
    @PostMapping
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) {
        Language createdLanguage = languageService.createLanguage(language);
        log.info("Created language: {} ({})", createdLanguage.getName(), createdLanguage.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLanguage);
    }
    
    // Update language
    @PutMapping("/{code}")
    public ResponseEntity<Language> updateLanguage(@PathVariable String code, @Valid @RequestBody Language language) {
        Language updatedLanguage = languageService.updateLanguage(code, language);
        log.info("Updated language: {} ({})", updatedLanguage.getName(), code);
        return ResponseEntity.ok(updatedLanguage);
    }
    
    // Delete language
    @DeleteMapping("/{code}")
    public ResponseEntity<Map<String, Object>> deleteLanguage(@PathVariable String code) {
        languageService.deleteLanguage(code);
        log.info("Deleted language: {}", code);
        return ResponseEntity.ok(Map.of("message", "Language deleted successfully", "code", code));
    }
    
    // Enable language
    @PatchMapping("/{code}/enable")
    public ResponseEntity<Map<String, Object>> enableLanguage(@PathVariable String code) {
        languageService.enableLanguage(code);
        log.info("Enabled language: {}", code);
        return ResponseEntity.ok(Map.of("message", "Language enabled successfully", "code", code));
    }
    
    // Disable language
    @PatchMapping("/{code}/disable")
    public ResponseEntity<Map<String, Object>> disableLanguage(@PathVariable String code) {
        languageService.disableLanguage(code);
        log.info("Disabled language: {}", code);
        return ResponseEntity.ok(Map.of("message", "Language disabled successfully", "code", code));
    }
    
    // Get default language
    @GetMapping("/default")
    public ResponseEntity<Language> getDefaultLanguage() {
        try {
            Optional<Language> defaultLangOpt = languageService.getDefaultLanguage();
            if (defaultLangOpt.isPresent()) {
                log.info("Retrieved default language: {}", defaultLangOpt.get().getCode());
                return ResponseEntity.ok(defaultLangOpt.get());
            } else {
                log.warn("No default language found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving default language", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Set default language
    @PatchMapping("/{code}/set-default")
    public ResponseEntity<?> setDefaultLanguage(@PathVariable String code) {
        try {
            languageService.setDefaultLanguage(code);
            log.info("Set default language: {}", code);
            return ResponseEntity.ok(Map.of("message", "Default language set successfully", "code", code));
        } catch (IllegalArgumentException e) {
            log.warn("Set default language failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage(), "timestamp", System.currentTimeMillis()));
        } catch (Exception e) {
            log.error("Error setting default language: {}", code, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Set default language failed", "timestamp", System.currentTimeMillis()));
        }
    }
    
    // Search languages
    @GetMapping("/search")
    public ResponseEntity<List<Language>> searchLanguages(@RequestParam(required = false) String q) {
        try {
            List<Language> languages = languageService.searchLanguages(q);
            log.info("Search returned {} languages for query: {}", languages.size(), q);
            return ResponseEntity.ok(languages);
        } catch (Exception e) {
            log.error("Error searching languages with query: {}", q, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get RTL languages
    @GetMapping("/rtl")
    public ResponseEntity<List<Language>> getRightToLeftLanguages() {
        try {
            List<Language> rtlLanguages = languageService.getRightToLeftLanguages();
            log.info("Retrieved {} RTL languages", rtlLanguages.size());
            return ResponseEntity.ok(rtlLanguages);
        } catch (Exception e) {
            log.error("Error retrieving RTL languages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get LTR languages
    @GetMapping("/ltr")
    public ResponseEntity<List<Language>> getLeftToRightLanguages() {
        try {
            List<Language> ltrLanguages = languageService.getLeftToRightLanguages();
            log.info("Retrieved {} LTR languages", ltrLanguages.size());
            return ResponseEntity.ok(ltrLanguages);
        } catch (Exception e) {
            log.error("Error retrieving LTR languages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Check if language is supported
    @GetMapping("/{code}/supported")
    public ResponseEntity<Map<String, Object>> checkLanguageSupport(@PathVariable String code) {
        try {
            boolean supported = languageService.isLanguageSupported(code);
            return ResponseEntity.ok(Map.of(
                "code", code,
                "supported", supported,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("Error checking language support: {}", code, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get language statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLanguageStatistics() {
        try {
            long enabledCount = languageService.getEnabledLanguagesCount();
            long disabledCount = languageService.getDisabledLanguagesCount();
            long totalCount = enabledCount + disabledCount;
            
            Map<String, Object> stats = Map.of(
                "total", totalCount,
                "enabled", enabledCount,
                "disabled", disabledCount,
                "timestamp", System.currentTimeMillis()
            );
            
            log.info("Language statistics: total={}, enabled={}, disabled={}", totalCount, enabledCount, disabledCount);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving language statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Translate text (existing endpoint - placeholder)
    @PostMapping("/translate")
    public ResponseEntity<?> translateText(@RequestBody Map<String, String> request) {
        try {
            String text = request.get("text");
            String from = request.get("from");
            String to = request.get("to");
            
            // Validate languages are supported
            if (!languageService.isLanguageSupported(from)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Source language not supported: " + from));
            }
            
            if (!languageService.isLanguageSupported(to)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Target language not supported: " + to));
            }
            
            // TODO: Implement actual translation logic
            Map<String, Object> response = Map.of(
                "originalText", text,
                "translatedText", "[TRANSLATED] " + text,
                "from", from,
                "to", to,
                "timestamp", System.currentTimeMillis()
            );
            
            log.info("Translation request: {} -> {}", from, to);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during translation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Translation failed", "timestamp", System.currentTimeMillis()));
        }
    }
}