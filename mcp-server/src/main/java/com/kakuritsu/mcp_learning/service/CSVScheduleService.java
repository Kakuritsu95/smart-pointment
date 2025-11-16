package com.kakuritsu.mcp_learning.service;

import com.kakuritsu.mcp_learning.dto.ScheduleCommand;
import com.kakuritsu.mcp_learning.model.CSVSchedule;
import com.kakuritsu.mcp_learning.repository.ScheduleRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CSVScheduleService implements ScheduleRepository {
    Path path;

    public CSVScheduleService() throws URISyntaxException {
        this.path = Paths.get(
                ClassLoader.getSystemResource("static/schedule.csv").toURI());
    }

    @Override
    public void findAll() {

    }

    @Override
    public void save(ScheduleCommand command) throws Exception {
        CSVSchedule schedule =  CSVSchedule.mapToCsvSchedule(command);
        ColumnPositionMappingStrategy<CSVSchedule> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(CSVSchedule.class);
        strategy.setColumnMapping(new String[]{"date", "reason", "time"});
        try (Writer writer = new FileWriter(path.toString(), true))
                {
                    StatefulBeanToCsv<CSVSchedule> sbc = new StatefulBeanToCsvBuilder<CSVSchedule>(writer)
                            .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .withMappingStrategy(strategy)
                            .build();
                    sbc.write(schedule);
        }
    }


}
