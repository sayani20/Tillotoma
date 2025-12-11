



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

import java.time.Duration;
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

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        List<Labour> labourList = labourRepo.findAll();

        for (Labour labour : labourList) {

            // Fetch today's punches
            List<AttendanceRaw> punches =
                    rawRepo.findByLabourIdAndLogDateTimeBetween(labour.getId(), start, end);

            if (punches.isEmpty()) continue;

            // Sort punches
            punches.sort(Comparator.comparing(AttendanceRaw::getLogDateTime));

            LocalTime inTime = punches.get(0).getLogDateTime().toLocalTime();
            LocalTime outTime = punches.get(punches.size() - 1).getLogDateTime().toLocalTime();

            // COMPUTE HOURS
            Duration duration = Duration.between(inTime, outTime);
            double hours = duration.toMinutes() / 60.0;

            // isCheck logic
            boolean computedIsCheck =
                    (hours >= 4 && hours <= 5) ||
                            (hours >= 8 && hours <= 9) ||
                            (hours >= 11 && hours <= 12) ||
                            (hours >= 14 && hours <= 15);

            // Billing
            double ratePerDay = labour.getRatePerDay() != null ? labour.getRatePerDay() : 0.0;
            if (ratePerDay <= 0 || hours <= 0) continue;

            double billAmount;

            if (hours >= 4 && hours <= 5)
                billAmount = ratePerDay / 2.0;
            else if (hours >= 8 && hours <= 9)
                billAmount = ratePerDay;
            else if (hours >= 11 && hours <= 12)
                billAmount = ratePerDay * 1.5;
            else if (hours >= 14 && hours <= 15)
                billAmount = ratePerDay * 2.0;
            else
                billAmount = (ratePerDay / 8.0) * hours;

            // SAVE / UPDATE
            LabourAttendance att = attendanceRepo
                    .findByLabourAndAttendanceDate(labour, today)
                    .orElse(null);

            if (att != null) {

                att.setInTime(inTime);
                att.setOutTime(outTime);
                att.setCalculatedAmount(billAmount);

                // ❗ Custom amount overwrite করব না যদি admin change করে থাকে
                if (att.getIsCustomUpdated() == null || !att.getIsCustomUpdated()) {
                    att.setCustomAmount(billAmount);
                }

                // ❗ isCheck overwrite করব না যদি admin change করে থাকে
                if (att.getIsCheckManuallyUpdated() == null || !att.getIsCheckManuallyUpdated()) {
                    att.setIsCheck(computedIsCheck);
                }

            } else {

                att = LabourAttendance.builder()
                        .labour(labour)
                        .attendanceDate(today)
                        .inTime(inTime)
                        .outTime(outTime)
                        .isPresent(true)
                        .calculatedAmount(billAmount)
                        .customAmount(billAmount)
                        .isCheck(computedIsCheck)
                        .build();
            }

            attendanceRepo.save(att);
        }

        System.out.println("Auto Attendance Processing Done For " + today);
    }


}
