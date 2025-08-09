package com.devwonder.language_service.config;

import com.devwonder.language_service.model.Language;
import com.devwonder.language_service.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final LanguageRepository languageRepository;

    @Override
    public void run(String... args) throws Exception {
        loadInitialLanguages();
    }

    private void loadInitialLanguages() {
        log.info("Loading initial languages...");

        // English (default)
        if (!languageRepository.existsByCode("en")) {
            Language english = new Language();
            english.setCode("en");
            english.setName("English");
            english.setNativeName("English");
            english.setFlagUrl("https://flagcdn.com/w40/us.png");
            english.setEnabled(true);
            english.setIsDefault(true);
            english.setSortOrder(1);
            english.setDirection("ltr");
            english.setCreatedAt(LocalDateTime.now());
            english.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(english);
            log.info("Created language: English (en) - DEFAULT");
        }

        // Vietnamese
        if (!languageRepository.existsByCode("vi")) {
            Language vietnamese = new Language();
            vietnamese.setCode("vi");
            vietnamese.setName("Vietnamese");
            vietnamese.setNativeName("Tiếng Việt");
            vietnamese.setFlagUrl("https://flagcdn.com/w40/vn.png");
            vietnamese.setEnabled(true);
            vietnamese.setIsDefault(false);
            vietnamese.setSortOrder(2);
            vietnamese.setDirection("ltr");
            vietnamese.setCreatedAt(LocalDateTime.now());
            vietnamese.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(vietnamese);
            log.info("Created language: Vietnamese (vi)");
        }

        // Chinese Simplified
        if (!languageRepository.existsByCode("zh")) {
            Language chinese = new Language();
            chinese.setCode("zh");
            chinese.setName("Chinese (Simplified)");
            chinese.setNativeName("简体中文");
            chinese.setFlagUrl("https://flagcdn.com/w40/cn.png");
            chinese.setEnabled(true);
            chinese.setIsDefault(false);
            chinese.setSortOrder(3);
            chinese.setDirection("ltr");
            chinese.setCreatedAt(LocalDateTime.now());
            chinese.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(chinese);
            log.info("Created language: Chinese Simplified (zh)");
        }

        // Japanese
        if (!languageRepository.existsByCode("ja")) {
            Language japanese = new Language();
            japanese.setCode("ja");
            japanese.setName("Japanese");
            japanese.setNativeName("日本語");
            japanese.setFlagUrl("https://flagcdn.com/w40/jp.png");
            japanese.setEnabled(true);
            japanese.setIsDefault(false);
            japanese.setSortOrder(4);
            japanese.setDirection("ltr");
            japanese.setCreatedAt(LocalDateTime.now());
            japanese.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(japanese);
            log.info("Created language: Japanese (ja)");
        }

        // Korean
        if (!languageRepository.existsByCode("ko")) {
            Language korean = new Language();
            korean.setCode("ko");
            korean.setName("Korean");
            korean.setNativeName("한국어");
            korean.setFlagUrl("https://flagcdn.com/w40/kr.png");
            korean.setEnabled(true);
            korean.setIsDefault(false);
            korean.setSortOrder(5);
            korean.setDirection("ltr");
            korean.setCreatedAt(LocalDateTime.now());
            korean.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(korean);
            log.info("Created language: Korean (ko)");
        }

        // Spanish
        if (!languageRepository.existsByCode("es")) {
            Language spanish = new Language();
            spanish.setCode("es");
            spanish.setName("Spanish");
            spanish.setNativeName("Español");
            spanish.setFlagUrl("https://flagcdn.com/w40/es.png");
            spanish.setEnabled(true);
            spanish.setIsDefault(false);
            spanish.setSortOrder(6);
            spanish.setDirection("ltr");
            spanish.setCreatedAt(LocalDateTime.now());
            spanish.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(spanish);
            log.info("Created language: Spanish (es)");
        }

        // French
        if (!languageRepository.existsByCode("fr")) {
            Language french = new Language();
            french.setCode("fr");
            french.setName("French");
            french.setNativeName("Français");
            french.setFlagUrl("https://flagcdn.com/w40/fr.png");
            french.setEnabled(true);
            french.setIsDefault(false);
            french.setSortOrder(7);
            french.setDirection("ltr");
            french.setCreatedAt(LocalDateTime.now());
            french.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(french);
            log.info("Created language: French (fr)");
        }

        // German
        if (!languageRepository.existsByCode("de")) {
            Language german = new Language();
            german.setCode("de");
            german.setName("German");
            german.setNativeName("Deutsch");
            german.setFlagUrl("https://flagcdn.com/w40/de.png");
            german.setEnabled(true);
            german.setIsDefault(false);
            german.setSortOrder(8);
            german.setDirection("ltr");
            german.setCreatedAt(LocalDateTime.now());
            german.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(german);
            log.info("Created language: German (de)");
        }

        // Arabic (RTL example)
        if (!languageRepository.existsByCode("ar")) {
            Language arabic = new Language();
            arabic.setCode("ar");
            arabic.setName("Arabic");
            arabic.setNativeName("العربية");
            arabic.setFlagUrl("https://flagcdn.com/w40/sa.png");
            arabic.setEnabled(true);
            arabic.setIsDefault(false);
            arabic.setSortOrder(9);
            arabic.setDirection("rtl");
            arabic.setCreatedAt(LocalDateTime.now());
            arabic.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(arabic);
            log.info("Created language: Arabic (ar) - RTL");
        }

        // Hebrew (RTL example)
        if (!languageRepository.existsByCode("he")) {
            Language hebrew = new Language();
            hebrew.setCode("he");
            hebrew.setName("Hebrew");
            hebrew.setNativeName("עברית");
            hebrew.setFlagUrl("https://flagcdn.com/w40/il.png");
            hebrew.setEnabled(false); // Disabled by default
            hebrew.setIsDefault(false);
            hebrew.setSortOrder(10);
            hebrew.setDirection("rtl");
            hebrew.setCreatedAt(LocalDateTime.now());
            hebrew.setUpdatedAt(LocalDateTime.now());
            
            languageRepository.save(hebrew);
            log.info("Created language: Hebrew (he) - RTL - DISABLED");
        }

        log.info("Initial languages loading completed.");
    }
}