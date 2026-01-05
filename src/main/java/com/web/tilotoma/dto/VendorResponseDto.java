package com.web.tilotoma.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendorResponseDto {

    private Long id;
    private String vendorName;
    private String contactPersonName;
    private String mobile;
    private String emailId;
    private String materialCategory;
    private String gstNumber;
    private String address;
    private Boolean isActive;
}
