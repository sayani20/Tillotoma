package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.VendorBillReportResponseDto;
import com.web.tilotoma.repository.VendorOrderRepository;
import com.web.tilotoma.repository.VendorPaymentRepository;
import com.web.tilotoma.service.VendorBillReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VendorBillReportServiceImpl implements VendorBillReportService {

    private final VendorOrderRepository vendorOrderRepository;
    private final VendorPaymentRepository paymentRepository;
    @Override
    public List<VendorBillReportResponseDto> getVendorBillReport(
            LocalDate fromDate,
            LocalDate toDate) {

        List<Object[]> rows =
                vendorOrderRepository.getVendorBillRaw(fromDate, toDate);

        List<VendorBillReportResponseDto> response = new ArrayList<>();

        // 🔑 vendor-wise running balance
        Map<Long, Double> vendorBalanceMap = new HashMap<>();

        for (Object[] r : rows) {

            Long vendorId = (Long) r[0];
            String vendorName = (String) r[1];
            Long orderId = (Long) r[2];
            String orderNumber = (String) r[3];
            String challanNumber = (String) r[4];
            Double orderAmount = (Double) r[5];
            Double paidAmount = (Double) r[6];
            LocalDate orderDate = (LocalDate) r[7];

            // 🧮 BUSINESS RULE
            double regularBalance = paidAmount - orderAmount;

            double previousFinal =
                    vendorBalanceMap.getOrDefault(vendorId, 0.0);

            double finalBalance =
                    previousFinal + regularBalance;

            vendorBalanceMap.put(vendorId, finalBalance);
            String paymentMode ="";

            response.add(
                    new VendorBillReportResponseDto(
                            vendorId,
                            vendorName,
                            orderId,
                            orderNumber,
                            challanNumber,
                            orderAmount,
                            paidAmount,
                            "",
                            regularBalance,
                            finalBalance,
                            orderDate
                    )
            );
        }

        return response;
    }
}

