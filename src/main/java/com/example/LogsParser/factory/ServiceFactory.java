package com.example.LogsParser.factory;

import com.example.LogsParser.parser.LogLineParser;
import com.example.LogsParser.service.LogParsingService;
import com.example.LogsParser.service.MessageDecodingService;
import com.example.LogsParser.service.PipelineReconstructionService;

public class ServiceFactory {

    public LogParsingService createLogParsingService() {
        LogLineParser parser = createLogLineParser();
        MessageDecodingService decodingService = createMessageDecodingService();
        PipelineReconstructionService reconstructionService = createPipelineReconstructionService();

        return new LogParsingService(parser, reconstructionService, decodingService);
    }

    protected LogLineParser createLogLineParser() {
        return new LogLineParser();
    }

    protected MessageDecodingService createMessageDecodingService() {
        return new MessageDecodingService();
    }

    protected PipelineReconstructionService createPipelineReconstructionService() {
        return new PipelineReconstructionService();
    }

}
