package com.kakuritsu.mcp_learning;

import com.kakuritsu.mcp_learning.mcp.tools.TestTools;
import com.kakuritsu.mcp_learning.repository.ScheduleRepository;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpLearningApplication {
	private final ScheduleRepository scheduleRepository;
	public static void main(String[] args) {
		SpringApplication.run(McpLearningApplication.class, args);
	}
	McpLearningApplication(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}
	@Bean
	public ToolCallbackProvider testToolsCo(TestTools testTools) throws Exception {
		return MethodToolCallbackProvider.builder().toolObjects(testTools).build();
	}

}
