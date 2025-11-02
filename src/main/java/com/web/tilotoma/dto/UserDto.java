package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
