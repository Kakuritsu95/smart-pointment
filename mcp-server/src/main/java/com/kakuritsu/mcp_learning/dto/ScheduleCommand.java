package com.kakuritsu.mcp_learning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.desktop.UserSessionEvent;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


public class ScheduleCommand {
    private LocalDate date;
    private String reason;
    private LocalTime time;

    public ScheduleCommand(LocalDate date, String reason, LocalTime time){
        this.date = date;
        this.reason = reason;
        this.time = time;
    }


    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public LocalTime getTime() {
        return time;
    }
}
