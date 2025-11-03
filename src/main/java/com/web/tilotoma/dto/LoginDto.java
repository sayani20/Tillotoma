package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
    private long roleid;
}
