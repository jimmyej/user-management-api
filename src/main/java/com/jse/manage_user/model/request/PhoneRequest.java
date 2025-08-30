package com.jse.manage_user.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhoneRequest {
    private String number;
    private String cityCode;
    private String countryCode;
}
