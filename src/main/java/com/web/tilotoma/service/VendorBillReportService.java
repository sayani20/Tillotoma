package com.web.tilotoma.service;




import com.web.tilotoma.dto.VendorBillReportResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface VendorBillReportService {



    List<VendorBillReportResponseDto> getVendorBillReport(
            LocalDate fromDate,
            LocalDate toDate
    );
}

