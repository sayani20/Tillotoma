package com.web.tilotoma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class VendorRequestDto {

    @NotBlank(message = "Vendor name is required")
    private String vendorName;

    private String contactPersonName;

    @Size(max = 15, message = "Mobile number max 15 digits")
    private String mobile;

    @Email(message = "Invalid email format")
    private String emailId;
    @NotBlank(message = "Material category is required")
    private String materialCategory;

    @Size(max = 20, message = "GST number max 20 characters")
    private String gstNumber;

    private String address;

}
