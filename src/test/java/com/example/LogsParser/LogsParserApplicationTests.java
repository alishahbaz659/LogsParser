package com.example.LogsParser;

import com.example.LogsParser.model.Pipeline;
import com.example.LogsParser.parser.LogLineParser;
import com.example.LogsParser.service.LogParsingService;
import com.example.LogsParser.service.MessageDecodingService;
import com.example.LogsParser.service.PipelineReconstructionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LogsParserApplicationTests {


	private LogParsingService logParsingService;

	@BeforeEach
	void setUp() {
		LogLineParser parser = new LogLineParser();
		MessageDecodingService decodingService = new MessageDecodingService();
		PipelineReconstructionService reconstructionService = new PipelineReconstructionService();
		logParsingService = new LogParsingService(parser, reconstructionService, decodingService);
	}

	@Test
	void testHexDecoding() {
		String hexData = "1 0 1 [48656C6C6F] -1"; // "Hello" in hex

		List<Pipeline> pipelines = logParsingService.parseLogContent(hexData);

		assertEquals(1, pipelines.size());
		assertEquals(1, pipelines.get(0).getMessages().size());
		assertEquals("Hello", pipelines.get(0).getMessages().get(0).getContent());
	}

}
