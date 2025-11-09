package com.kakuritsu.mcp_learning.repository;

import com.kakuritsu.mcp_learning.dto.ScheduleCommand;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository {
    void findAll();
    void save(ScheduleCommand command) throws Exception;
}
