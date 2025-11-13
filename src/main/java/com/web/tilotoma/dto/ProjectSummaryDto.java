package com.web.tilotoma.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSummaryDto {
    private Long id;
    private String projectName;
}
