package com.jse.manage_user.validator;

import com.jse.manage_user.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    @Value("${user.validation.email.regex}")
    private String emailRegexValue;

    @Value("${user.validation.password.regex}")
    private String passwordRegexValue;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequest user = (UserRequest) target;
        //Validating email format
        if (user.getEmail() == null || !Pattern.compile(emailRegexValue).matcher(user.getEmail()).matches()) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }
        //Validating password requirements
        if (user.getPassword() == null || !Pattern.compile(passwordRegexValue).matcher(user.getPassword()).matches()) {
            errors.rejectValue("password", "password.weak",
                    "Password must be at least 8 characters, contain one uppercase, one number, and one special character");
        }
    }
}
