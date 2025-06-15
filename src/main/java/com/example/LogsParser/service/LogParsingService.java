package com.example.LogsParser.service;

import com.example.LogsParser.model.LogMessage;
import com.example.LogsParser.model.Pipeline;
import com.example.LogsParser.parser.LogLineParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for orchestrating log parsing workflow
 */
public class LogParsingService {

    private final LogLineParser logLineParser;
    private final PipelineReconstructionService reconstructionService;
    private final MessageDecodingService decodingService;


    public LogParsingService(LogLineParser logLineParser,
                             PipelineReconstructionService reconstructionService,
                             MessageDecodingService decodingService) {
        this.logLineParser = logLineParser;
        this.reconstructionService = reconstructionService;
        this.decodingService = decodingService;
    }

    public List<Pipeline> parseLogContent(String logContent) {
        if (logContent == null || logContent.trim().isEmpty()) {
            System.out.println("Warning: Empty log content provided");
            return new ArrayList<>();
        }

        List<LogMessage> validMessages = parseAndDecodeMessages(logContent);

        Map<String, List<LogMessage>> messagesByPipeline = validMessages.stream()
                .collect(Collectors.groupingBy(LogMessage::getPipelineId));

        List<Pipeline> pipelines = reconstructionService.reconstructPipelines(messagesByPipeline);

        System.out.println("Successfully parsed " + pipelines.size() + " pipelines from " + validMessages.size() + " valid messages");

        return pipelines;

    }


    private List<LogMessage> parseAndDecodeMessages(String logContent) {
        List<LogMessage> validMessages = new ArrayList<>();
        String[] lines = logContent.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (line.trim().isEmpty()) {
                continue;
            }

            try {
                LogMessage message = logLineParser.parseLogLine(line);

                // Decode content using a strategy pattern
                String decodedContent = decodingService.decode(
                        message.getContent(),
                        message.getEncoding()
                );
                message.setContent(decodedContent);

                validMessages.add(message);

            } catch (Exception e) {
                System.out.println("Line " + (i + 1) + ": Skipping invalid line '" + line + "' - Error: " + e.getMessage());
            }
        }

        System.out.println("Parsed " + validMessages.size() + " valid messages from " + lines.length + " total lines");

        return validMessages;
    }

}
