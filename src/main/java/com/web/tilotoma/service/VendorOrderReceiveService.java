package com.web.tilotoma.service;
import com.web.tilotoma.dto.MaterialReceiveRequestDto;
import com.web.tilotoma.dto.MaterialReceiveResponseDto;
import com.web.tilotoma.dto.VendorOrderReceiveDto;

import java.util.List;
public interface  VendorOrderReceiveService {
    MaterialReceiveResponseDto receiveMaterial(MaterialReceiveRequestDto request);

    List<VendorOrderReceiveDto.ReceiveResponse> getReceivedByOrder(Long orderId);
}
