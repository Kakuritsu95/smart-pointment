package com.kakuritsu.mcp_learning.mcp.tools;

import com.kakuritsu.mcp_learning.dto.ScheduleCommand;
import com.kakuritsu.mcp_learning.repository.ScheduleRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TestTools {
    private final ScheduleRepository repository;

    TestTools(ScheduleRepository repository){
        this.repository = repository;
    }
    @Tool(description = "Retrieves schedule for monday for Thodoris")
    public String retrieveMondaySchedule(){
        return "Thodoris is very busy on mondays he has to wake up early and to voskisei ta provata";
    }

    @Tool(description = "Schedule an appointment with Thodoris for a specific date and time")
    public String doAppointment(
            @ToolParam(description = "The date of the appointment eg 2024-03-01") LocalDate appointmentDate,
            @ToolParam(description = "The time for the appointment in format HH:mm eg 14:50") LocalTime time,
            @ToolParam(description = "A short reason or title for the appointment eg meet each other") String reason
    ) throws Exception {
        ScheduleCommand scheduleCommand = new ScheduleCommand(appointmentDate, reason, time);
        repository.save(scheduleCommand);
        return String.format("Appointment saved for %s at %s: %s", appointmentDate, time, reason);
    }
}
