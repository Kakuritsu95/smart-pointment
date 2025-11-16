package com.kakuritsu.mcp_learning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakuritsu.mcp_learning.mcp.tools.TestTools;
import com.kakuritsu.mcp_learning.repository.ScheduleRepository;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

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

	@Bean
	public List<McpStatelessServerFeatures.SyncResourceSpecification> myResources() {
		McpSchema.Resource systemInfoResource = McpSchema.Resource.builder().uri("file:///schedule.csv").name("thodoris-schedule").description("the schedule of Thodoris").mimeType("csv").build();
		var resourceSpecification = new McpStatelessServerFeatures.SyncResourceSpecification(systemInfoResource, (context, request) -> {
			try {
				var systemInfo = Map.of();
				String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
				return new McpSchema.ReadResourceResult(
						List.of(new McpSchema.TextResourceContents(request.uri(), "csv", jsonContent)));
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to generate system info", e);
			}
		});

		return List.of(resourceSpecification);

	}

}
