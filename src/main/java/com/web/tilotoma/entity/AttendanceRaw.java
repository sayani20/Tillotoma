package com.web.tilotoma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "atten")
@Data


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Access(AccessType.FIELD)

public class AttendanceRaw {

    /*@Id
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
    private String dName;*/




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "EmployeeCode")
    private Long labourId;
    @Column(name = "LogDateTime")
    private LocalDateTime logDateTime;
    @Column(name = "LogDate")
    private Date logDate;
    @Column(name = "LogTime")
    private Time logTime;
    @Column(name = "Direction")
    private String direction;
    @Column(name = "Dname")
    private String dName;





}
