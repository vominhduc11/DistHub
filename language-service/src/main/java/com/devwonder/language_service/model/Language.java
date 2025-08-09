package com.devwonder.language_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "languages")
public class Language {
    
    @Id
    @Column(name = "code", length = 10)
    private String code; // "en", "vi", "fr", "zh", "ja"
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // "English", "Tiếng Việt", "Français"
    
    @Column(name = "native_name", length = 100)
    private String nativeName; // "English", "Tiếng Việt", "Français"
    
    @Column(name = "flag_url", length = 500)
    private String flagUrl; // URL to flag icon
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "direction", length = 3)
    private String direction = "ltr"; // "ltr" or "rtl"
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isRightToLeft() {
        return "rtl".equals(direction);
    }
    
    public boolean isLeftToRight() {
        return "ltr".equals(direction);
    }
}