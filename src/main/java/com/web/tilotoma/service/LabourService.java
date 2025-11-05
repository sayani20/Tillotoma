package com.web.tilotoma.service;

import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.entity.Labour;

public interface LabourService {
    public Labour getLabourById(Long id);
    public Labour updateLabour(Long id, LabourRequest labourRequest);
    public String deleteLabour(Long id);
}
