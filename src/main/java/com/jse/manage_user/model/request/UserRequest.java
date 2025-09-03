package com.jse.manage_user.model.request;

import com.jse.manage_user.validator.ValidUserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@ValidUserRequest
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotEmpty(message = "No phone number detected!")
    private Set<PhoneRequest> phones;

}
