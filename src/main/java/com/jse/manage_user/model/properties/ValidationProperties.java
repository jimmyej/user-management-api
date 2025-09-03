package com.jse.manage_user.model.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {

    private String emailRegex;
    private PasswordProperties password = new PasswordProperties();

    @Getter
    @Setter
    public static class PasswordProperties {
        private String lowercase;
        private String uppercase;
        private String digit;
        private String special;
        private int minLength;
    }

    public String getEmailRegex() { return emailRegex; }
    public void setEmailRegex(String emailRegex) { this.emailRegex = emailRegex; }

    public PasswordProperties getPassword() { return password; }
    public void setPassword(PasswordProperties password) { this.password = password; }
}
