package com.kakuritsu.mcp_learning.mcp.resources;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

@Component
public class TestResource {
    Path path;
    TestResource() throws URISyntaxException {
        path = Paths.get(ClassLoader.getSystemResource("static/schedule.csv").toURI());
    }
    @McpResource(
            uri = "file:///schedule.csv",
            name = "Thodoris schedule",
            description = "Provide the schedule for Thodoris")
    public McpSchema.ReadResourceResult getThodorisSchedule(String username) throws FileNotFoundException {
        File scheduleCsvFile = new File(path.toString());
        StringBuilder data = new StringBuilder();
        try(Scanner scanner = new Scanner(scheduleCsvFile)) {
            while (scanner.hasNextLine()){
                data.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e){
            System.out.println("The schedule file was not found.");
        }
        return new McpSchema.ReadResourceResult(List.of(
                new McpSchema.TextResourceContents (
                        "file:///schedule.csv",
                        "csv",
                        data.toString())
        ));
    }

    @McpResource(
            uri = "file:///schedule.blob",
            name = "Thodoris schedule",
            description = "Provide the schedule for Thodoris")
    public McpSchema.ReadResourceResult getUserProfile(String username) throws IOException {
        byte[] blobData = Files.readAllBytes(path);


        return new McpSchema.ReadResourceResult(List.of(
                new McpSchema.BlobResourceContents (
                        "file:///schedule.blob",
                        "blob",
                        Base64.getEncoder().encodeToString(blobData)
                        )
        ));
    }


}



