package com.web.tilotoma.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorResponse {
    private Long id;
    private String contractorName;
    private String username;
    private String email;
    private String mobileNumber;
    private Boolean isActive;
    private LocalDateTime createdOn;

    private Long labourCount;      // মোট লেবার সংখ্যা
    private Long labourTypeCount;  // মোট আলাদা টাইপ সংখ্যা
}
