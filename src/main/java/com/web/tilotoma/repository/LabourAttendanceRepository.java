package com.web.tilotoma.repository;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.entity.Labour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabourAttendanceRepository extends JpaRepository<LabourAttendance, Long> {
    List<LabourAttendance> findByLabour(Labour labour);
    List<LabourAttendance> findByAttendanceDate(LocalDate date);
    List<LabourAttendance> findByLabourAndAttendanceDate(Labour labour, LocalDate date);
    Optional<LabourAttendance> findByLabourIdAndAttendanceDate(Long labourId, LocalDate attendanceDate);
    List<LabourAttendance> findByLabourIdOrderByAttendanceDateDesc(Long labourId);
}