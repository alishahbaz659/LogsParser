package com.example.LogsParser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO for pipeline data
 * Represents a complete reconstructed pipeline with logs in reverse order
 */
@Data
@NoArgsConstructor
public class Pipeline {

    private String id;

    private List<LogMessage> messages = new ArrayList<>();

    public Pipeline(String id) {
        this.id = id;
    }

}
