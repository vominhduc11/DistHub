package com.devwonder.language_service.service;

import com.devwonder.language_service.exception.DefaultLanguageException;
import com.devwonder.language_service.exception.LanguageAlreadyExistsException;
import com.devwonder.language_service.exception.LanguageNotFoundException;
import com.devwonder.language_service.model.Language;
import com.devwonder.language_service.repository.LanguageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class LanguageService {
    
    private final LanguageRepository languageRepository;
    
    // Basic CRUD operations
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
    
    public List<Language> getSupportedLanguages() {
        return languageRepository.findByEnabledTrueOrderBySortOrder();
    }
    
    public Optional<Language> getLanguageByCode(String code) {
        return languageRepository.findByCode(code);
    }
    
    @Transactional
    public Language createLanguage(@Valid Language language) {
        if (languageRepository.existsByCode(language.getCode())) {
            throw new LanguageAlreadyExistsException(language.getCode());
        }
        
        log.info("Creating new language: {} ({})", language.getName(), language.getCode());
        return languageRepository.save(language);
    }
    
    @Transactional
    public Language updateLanguage(String code, @Valid Language updatedLanguage) {
        Language existing = findLanguageByCodeOrThrow(code);
        
        existing.setName(updatedLanguage.getName());
        existing.setNativeName(updatedLanguage.getNativeName());
        existing.setFlagUrl(updatedLanguage.getFlagUrl());
        existing.setEnabled(updatedLanguage.getEnabled());
        existing.setIsDefault(updatedLanguage.getIsDefault());
        existing.setSortOrder(updatedLanguage.getSortOrder());
        existing.setDirection(updatedLanguage.getDirection());
        existing.setUpdatedAt(LocalDateTime.now());
        
        log.info("Updating language: {} ({})", existing.getName(), existing.getCode());
        return languageRepository.save(existing);
    }
    
    @Transactional
    public void deleteLanguage(String code) {
        Language language = findLanguageByCodeOrThrow(code);
        
        if (language.getIsDefault()) {
            throw DefaultLanguageException.cannotDelete(code);
        }
        
        log.info("Deleting language: {}", code);
        languageRepository.deleteById(code);
    }
    
    // Status operations
    @Transactional
    public void enableLanguage(String code) {
        Language language = findLanguageByCodeOrThrow(code);
        language.setEnabled(true);
        language.setUpdatedAt(LocalDateTime.now());
        
        log.info("Enabling language: {} ({})", language.getName(), code);
        languageRepository.save(language);
    }
    
    @Transactional
    public void disableLanguage(String code) {
        Language language = findLanguageByCodeOrThrow(code);
        
        if (language.getIsDefault()) {
            throw DefaultLanguageException.cannotDisable(code);
        }
        
        language.setEnabled(false);
        language.setUpdatedAt(LocalDateTime.now());
        
        log.info("Disabling language: {} ({})", language.getName(), code);
        languageRepository.save(language);
    }
    
    // Default language operations
    public Optional<Language> getDefaultLanguage() {
        return languageRepository.findByIsDefaultTrueAndEnabledTrue();
    }
    
    @Transactional
    public void setDefaultLanguage(String code) {
        // First, unset current default
        Optional<Language> currentDefaultOpt = getDefaultLanguage();
        if (currentDefaultOpt.isPresent()) {
            Language currentDefault = currentDefaultOpt.get();
            currentDefault.setIsDefault(false);
            currentDefault.setUpdatedAt(LocalDateTime.now());
            languageRepository.save(currentDefault);
        }
        
        // Set new default
        Language newDefault = findLanguageByCodeOrThrow(code);
        newDefault.setIsDefault(true);
        newDefault.setEnabled(true); // Default language must be enabled
        newDefault.setUpdatedAt(LocalDateTime.now());
        
        log.info("Setting default language: {} ({})", newDefault.getName(), code);
        languageRepository.save(newDefault);
    }
    
    // Search operations
    public List<Language> searchLanguages(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getSupportedLanguages();
        }
        return languageRepository.findByEnabledTrueAndSearchTerm(searchTerm.trim());
    }
    
    // Direction operations
    public List<Language> getRightToLeftLanguages() {
        return languageRepository.findRightToLeftLanguages();
    }
    
    public List<Language> getLeftToRightLanguages() {
        return languageRepository.findLeftToRightLanguages();
    }
    
    // Statistics
    public long getEnabledLanguagesCount() {
        return languageRepository.countEnabledLanguages();
    }
    
    public long getDisabledLanguagesCount() {
        return languageRepository.countDisabledLanguages();
    }
    
    // Validation helpers
    public boolean isLanguageSupported(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        Optional<Language> languageOpt = languageRepository.findByCode(code);
        return languageOpt.isPresent() && languageOpt.get().getEnabled();
    }
    
    public boolean existsByCode(String code) {
        return languageRepository.existsByCode(code);
    }
    
    // Helper method to reduce code duplication
    private Language findLanguageByCodeOrThrow(String code) {
        return languageRepository.findByCode(code)
            .orElseThrow(() -> new LanguageNotFoundException(code));
    }
}