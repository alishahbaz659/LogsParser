package com.example.LogsParser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO for message data
 * Format: pipeline_id id encoding [body] next_id
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {

    private String id;

    private String pipelineId;

    private String content;

    private Integer encoding;

    private String nextId;

    //For debugging only
    private String originalLine;

}
