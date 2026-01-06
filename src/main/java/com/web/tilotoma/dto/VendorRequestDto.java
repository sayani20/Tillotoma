package com.web.tilotoma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.util.List;

@Data
public class VendorRequestDto {
    private Long id;
    private String vendorName;
    private String contactPersonName;
    private String mobile;
    private String emailId;

    private Long materialCategoryId;   // ← parent table theke asbe
    private List<Long> materialIds;    // ← oi category er materials

    private String gstNumber;
    private String address;
}
