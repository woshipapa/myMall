package com.papa.dto;

import javax.validation.constraints.NotEmpty;

public class AdminParam {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String email;
    private String nickName;
    private String note;

    private String icon;


}
