package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.entity.AttendanceRaw;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.repository.AttendanceRawRepository;
import com.web.tilotoma.repository.LabourAttendanceRepository;
import com.web.tilotoma.repository.LabourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AutoAttendanceProcessor {

    @Autowired
    private AttendanceRawRepository rawRepo;

    @Autowired
    private LabourRepository labourRepo;

    @Autowired
    private LabourAttendanceRepository attendanceRepo;


    @Scheduled(fixedDelay = 10000) // Every 10 seconds
    public void processDailyAttendance() {

        List<Labour> labourList = labourRepo.findAll();

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        for (Labour labour : labourList) {

            List<AttendanceRaw> punches =
                    rawRepo.findByLabourIdAndLogDateTimeBetween(
                            labour.getId(), start, end);

            if (punches.isEmpty()) continue;

            punches.sort(Comparator.comparing(AttendanceRaw::getLogDateTime));

            LocalTime inTime = punches.get(0).getLogDateTime().toLocalTime();
            LocalTime outTime = punches.get(punches.size() - 1).getLogDateTime().toLocalTime();

            Optional<LabourAttendance> existing =
                    attendanceRepo.findByLabourAndAttendanceDate(labour, today);

            LabourAttendance att;

            if (existing.isPresent()) {
                att = existing.get();
                att.setInTime(inTime);
                att.setOutTime(outTime);
            } else {
                att = LabourAttendance.builder()
                        .labour(labour)
                        .attendanceDate(today)
                        .inTime(inTime)
                        .outTime(outTime)
                        .isCheck(true)
                        .isPresent(true)
                        .build();
            }

            attendanceRepo.save(att);
        }

        System.out.println("Auto Attendance Processing Done For " + today);
    }
}
