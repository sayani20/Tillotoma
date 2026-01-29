package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class ReceiveResponseDto {
    private LocalDate receivedDate;

    private Long vendorId;
    private String vendorName;

    private Long materialId;
    private String materialName;

    private Double quantity;
    private String challanNumber;
}
