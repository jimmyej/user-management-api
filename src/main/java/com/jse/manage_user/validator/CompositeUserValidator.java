package com.jse.manage_user.validator;

import com.jse.manage_user.model.properties.ValidationProperties;
import com.jse.manage_user.model.request.UserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
public class CompositeUserValidator implements ConstraintValidator<ValidUserRequest, UserRequest> {

    private final ValidationProperties properties;

    @Autowired
    CompositeUserValidator(ValidationProperties properties){
        this.properties = properties;
    }


    public boolean isValid(UserRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if (request.getEmail() == null || !request.getEmail().matches(properties.getEmailRegex()) || !request.getEmail().contains(".")) {
            context.buildConstraintViolationWithTemplate("Invalid email format")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valid = false;
        }

        String password = request.getPassword();

        if (password == null ||
                password.length() < properties.getPassword().getMinLength() ||
                !password.matches(properties.getPassword().getLowercase()) ||
                !password.matches(properties.getPassword().getUppercase()) ||
                !password.matches(properties.getPassword().getDigit()) ||
                !password.matches(properties.getPassword().getSpecial())) {

            context.buildConstraintViolationWithTemplate(
                            "Password must be at least " + properties.getPassword().getMinLength() +
                                    " chars, contain uppercase, lowercase, digit, and special char")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
