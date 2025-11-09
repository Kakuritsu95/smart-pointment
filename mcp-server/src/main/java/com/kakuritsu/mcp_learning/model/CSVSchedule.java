package com.kakuritsu.mcp_learning.model;

import com.kakuritsu.mcp_learning.dto.ScheduleCommand;
import com.opencsv.bean.CsvBindByName;

public class CSVSchedule {
    @CsvBindByName(column = "date")
    String date;

    @CsvBindByName(column = "reason")
    String reason;

    @CsvBindByName(column = "time")
    String time;

    public CSVSchedule() {
    }

    public CSVSchedule(String date, String reason, String time) {
        this.date = date;
        this.reason = reason;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getTime() {
        return time;
    }

    public static CSVSchedule mapToCsvSchedule(ScheduleCommand command) {
        return new CSVSchedule(command.getDate().toString(), command.getReason(), command.getTime().toString());
    }
}
