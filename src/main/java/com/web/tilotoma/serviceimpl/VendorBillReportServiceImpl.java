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

            Long vendorId = ((Number) r[0]).longValue();
            String vendorName = (String) r[1];
            Long orderId = ((Number) r[2]).longValue();
            String orderNumber = (String) r[3];
            String challanNumber = (String) r[4];

            Double totalAmount =
                    r[5] == null ? 0.0 : ((Number) r[5]).doubleValue();

            Double paidAmount =
                    r[6] == null ? 0.0 : ((Number) r[6]).doubleValue();

            String paymentMode = (String) r[7];

            // 🔥 SAFE LocalDate handling (NO ClassCastException)
            LocalDate orderDate;
            Object dateObj = r[8];

            if (dateObj instanceof LocalDate) {
                orderDate = (LocalDate) dateObj;
            } else if (dateObj instanceof java.sql.Date) {
                orderDate = ((java.sql.Date) dateObj).toLocalDate();
            } else if (dateObj instanceof String) {
                orderDate = LocalDate.parse((String) dateObj);
            } else {
                orderDate = null;
            }

            // 🧮 BUSINESS LOGIC
            double regularBalance = paidAmount - totalAmount;

            double previousFinal =
                    vendorBalanceMap.getOrDefault(vendorId, 0.0);

            double finalBalance = previousFinal + regularBalance;

            vendorBalanceMap.put(vendorId, finalBalance);

            response.add(
                    new VendorBillReportResponseDto(
                            vendorId,
                            vendorName,
                            orderId,
                            orderNumber,
                            challanNumber,
                            totalAmount,
                            paidAmount,
                            paymentMode,
                            regularBalance,
                            finalBalance,
                            orderDate
                    )
            );
        }

        return response;
    }
}

