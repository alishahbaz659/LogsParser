package com.example.LogsParser.parser;

import com.example.LogsParser.model.LogMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for individual log lines using regex pattern matching
 * Format: pipeline_id id encoding [body] next_id
 */
public class LogLineParser {


    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+\\[([^\\]]*)\\]\\s+(\\S+)$"
    );


    public LogMessage parseLogLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Log line cannot be null or empty");
        }

        String trimmedLine = line.trim();
        Matcher matcher = LOG_PATTERN.matcher(trimmedLine);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid log line format: " + line);
        }

        try {
            String pipelineId = matcher.group(1);
            String messageId = matcher.group(2);
            String encodingStr = matcher.group(3);
            String body = matcher.group(4);
            String nextId = matcher.group(5);

            // Validate encoding is a valid integer
            Integer encoding = Integer.parseInt(encodingStr);
            if (encoding < 0 || encoding > 1) {
                throw new IllegalArgumentException("Invalid encoding type: " + encoding + ". Must be 0 or 1");
            }

            return LogMessage.builder()
                    .pipelineId(pipelineId)
                    .id(messageId)
                    .encoding(encoding)
                    .content(body)
                    .nextId(nextId)
                    .originalLine(trimmedLine)
                    .build();

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid encoding format in line: " + line, e);
        }
    }


}
