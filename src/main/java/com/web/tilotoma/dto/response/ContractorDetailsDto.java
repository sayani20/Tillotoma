package com.web.tilotoma.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractorDetailsDto {
    private Long id;
    private String contractorName;
    private String username;
    private String email;
    private String mobileNumber;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdOn;

    private List<LabourDto> labours;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabourDto {
        private Long id;
        private String labourName;
        private String email;
        private String mobileNumber;
        private Long labourTypeId;
        private String labourTypeName;
        private Boolean isActive;
        private LocalDateTime createdOn;
    }
}
