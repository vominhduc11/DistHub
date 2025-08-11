package com.devwonder.notification_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class RSAKeyProperties {

    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            loadPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA keys", e);
        }
    }

    private void loadPublicKey() throws Exception {
        Resource resource = new ClassPathResource("public_key.pem");
        String publicKeyContent = new String(resource.getInputStream().readAllBytes())
                .replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // Remove any remaining whitespace

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);
    }
}