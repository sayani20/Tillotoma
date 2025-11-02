package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    private Long userId;
    private Boolean isActive;
    private String remarks;
}
