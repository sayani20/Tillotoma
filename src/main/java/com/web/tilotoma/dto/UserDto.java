package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private String aadharNumber;
    private Long roleId;
    private Long createdBy;
    private String lastUpdateRemarks;
}
