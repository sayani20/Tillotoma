package com.web.tilotoma.service;

import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourResponse;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.dto.response.LabourResponseDto;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourType;

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
}
