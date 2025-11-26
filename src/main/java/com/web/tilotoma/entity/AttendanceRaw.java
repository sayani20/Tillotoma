package com.web.tilotoma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "atten")
@Data
public class AttendanceRaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_code")
    private Long labourId;
    @Column(name = "log_date_time")
    private LocalDateTime logDateTime;
    @Column(name = "log_date")
    private Date logDate;
    @Column(name = "log_time")
    private Time logTime;
    @Column(name = "direction")
    private String direction;
    @Column(name = "dname")
    private String dName;
}
