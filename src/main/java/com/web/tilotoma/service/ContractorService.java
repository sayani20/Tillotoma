package com.web.tilotoma.service;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.bill.ContractorBillingResponse;
import com.web.tilotoma.dto.bill.LabourBillingDetailsResponse;
import com.web.tilotoma.dto.response.ContractorDetailsDto;

import com.web.tilotoma.entity.labour.Contractor;
import com.web.tilotoma.entity.labour.LabourAttendance;

import java.time.LocalDate;
import java.util.List;


public interface ContractorService {


    public Contractor addContractor(ContractorRequest req) ;

    // Get All Contractors
    public List<ContractorResponse> getAllContractorsCustom();

    public LabourAttendance markAttendance(Long labourId) ;

    public List<LabourAttendance> getAttendanceByLabour(Long labourId);

    public ContractorDetailsDto getContractorById(Long contractorId);
    public Contractor updateContractorDetails(Long contractorId, ContractorRequest request);
    public void deleteContractorDetails(Long contractorId);

    List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(Long contractorId, LocalDate startDate, LocalDate endDate);
    public List<ContractorProjectMonthlyReportDto> getMonthlyProjectReport(Long contractorId, int year, int month);


    List<LabourBillingDetailsResponse> getLabourBillingDetails(Long contractorId, Long projectId, LocalDate fromDate, LocalDate toDate);

    ApiResponse<List<ContractorBillingResponse>> getBillingReport(LocalDate fromDate, LocalDate toDate);

    String updateIsCheck(Long labourId, LocalDate attendanceDate, Boolean isCheck);


    ApiResponse<String> updateContractorPayment(PaymentRequest request);

    ApiResponse<List<ContractorPaymentResponse>> getPaymentHistory(LocalDate fromDate, LocalDate toDate);

}
