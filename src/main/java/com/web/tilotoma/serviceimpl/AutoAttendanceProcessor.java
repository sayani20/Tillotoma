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

            // ====================== COMPUTE HOURS ======================
            Duration duration = Duration.between(inTime, outTime);
            long minutes = duration.toMinutes();
            double hours = minutes / 60.0;

            boolean computedIsCheck;

            // ====================== isCheck LOGIC ======================
            if ((hours >= 4 && hours <= 5) ||
                    (hours >= 8 && hours <= 9) ||
                    (hours >= 11 && hours <= 12) ||
                    (hours >= 14 && hours <= 15)) {

                computedIsCheck = true;
            } else {
                computedIsCheck = false;
            }

            Double ratePerDay = labour.getRatePerDay() != null ? labour.getRatePerDay() : 0.0;
            double billAmount = 0.0;

            if (ratePerDay > 0 && hours > 0) {

                double h = hours;

                if (h >= 4.0 && h <= 5.0) {
                    billAmount = ratePerDay / 2.0;

                } else if (h >= 8.0 && h <= 9.0) {
                    billAmount = ratePerDay;

                } else if (h >= 11.0 && h <= 12.0) {
                    billAmount = ratePerDay * 1.5;

                } else if (h >= 14.0 && h <= 15.0) {
                    billAmount = ratePerDay * 2.0;

                } else {
                    billAmount = (ratePerDay / 8.0) * h;
                }
            }

            // ====================== SAVE ATTENDANCE ======================
            Optional<LabourAttendance> existing =
                    attendanceRepo.findByLabourAndAttendanceDate(labour, today);

            LabourAttendance att;

            if (existing.isPresent()) {
                att = existing.get();

                att.setInTime(inTime);
                att.setOutTime(outTime);
                att.setIsCheck(computedIsCheck);

                // Update calculated amount (system generated)
                att.setCalculatedAmount(billAmount);

                // If custom amount is NULL keep it equal to calculated amount
                if (att.getCustomAmount() == null) {
                    att.setCustomAmount(billAmount);
                }

            } else {

                att = LabourAttendance.builder()
                        .labour(labour)
                        .attendanceDate(today)
                        .inTime(inTime)
                        .outTime(outTime)
                        .isPresent(true)
                        .isCheck(computedIsCheck)
                        .calculatedAmount(billAmount)
                        .customAmount(billAmount)  // initially same as calculated
                        .build();
            }

            attendanceRepo.save(att);
        }
        System.out.println("Auto Attendance Processing Done For " + today);
    }

}
