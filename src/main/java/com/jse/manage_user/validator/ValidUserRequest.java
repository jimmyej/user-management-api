package com.jse.manage_user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompositeUserValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRequest {
    String message() default "Business validation failed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
