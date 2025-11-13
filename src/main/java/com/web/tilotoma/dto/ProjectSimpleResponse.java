package com.web.tilotoma.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectSimpleResponse {
    private Long id;
    private String name;
    private String location;
    private String city;
    private String state;
    private Boolean isActive;
}
