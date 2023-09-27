package com.papa.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class UmsAdminPasswordParam {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String rawPassword;
    @NotEmpty
    private String newPassword;


}
