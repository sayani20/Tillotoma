package com.web.tilotoma.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MaterialCategoryResponseDto {
    private Long id;
    private String name;
    private Boolean isActive;
    private LocalDateTime createdOn;
}