package com.web.tilotoma.repository;

import com.web.tilotoma.entity.AttendanceRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRawRepository extends JpaRepository<AttendanceRaw, Long> {

    List<AttendanceRaw> findByLabourIdAndLogDateTimeBetween(Long labourId, LocalDateTime start, LocalDateTime end);
}

