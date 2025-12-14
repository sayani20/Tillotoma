package com.web.tilotoma.service;

import com.web.tilotoma.dto.LabourMonthlyReportDto;
import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourResponse;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.dto.response.LabourResponseDto;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourType;

import java.time.LocalDate;
import java.util.List;

public interface LabourService {
    public Labour getLabourById(Long id);
    public Labour updateLabour(Long id, LabourRequest labourRequest);
    public String deleteLabour(Long id);

    // Get All Labours
    public List<LabourResponse> getAllLabours();
    public List<LabourResponseDto> getLaboursByContractor(Long contractorId) ;

    // Add Labour Under Contractor
    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req);

    // Get All Labour Types
    public List<LabourType> getAllLabourTypes();

    // Add Labour Type
    public LabourType addLabourType(LabourTypeRequest req) ;

    public List<LabourMonthlyReportDto> getMonthlyReport(Long contractorId, int year, int month);


    LabourType updateLabourType(Long id, LabourTypeRequest req);
    LabourType toggleLabourTypeStatus(Long id, boolean isActive);
    String deleteLabourType(Long id);
    String updateCustomAmount(Long labourId, LocalDate attendanceDate, Double customAmount,String paymentMethod, String remarks);


}
