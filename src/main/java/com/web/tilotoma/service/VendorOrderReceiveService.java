package com.web.tilotoma.service;
import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.material.VendorOrderReceive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface  VendorOrderReceiveService {
    MaterialReceiveResponseDto receiveMaterial(MaterialReceiveRequestDto request);

    List<VendorOrderReceiveDto.ReceiveResponse> getReceivedByOrder(Long orderId);
    List<Map<String, Object>> getMaterialStockById(Long materialId);

    List<VendorOrderReceiveResponseDto> getReceivedOrders(
            LocalDate fromDate,
            LocalDate toDate
    );



    List<ReceiveResponseDto> getReceives(
            Long materialId,
            LocalDate fromDate,
            LocalDate toDate
    );
}
