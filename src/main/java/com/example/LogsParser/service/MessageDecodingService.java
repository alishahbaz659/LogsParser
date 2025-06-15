package com.example.LogsParser.service;

import com.example.LogsParser.strategy.AsciiDecodingStrategy;
import com.example.LogsParser.strategy.DecodingStrategy;
import com.example.LogsParser.strategy.HexDecodingStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Context/Coordinator class for decoding strategies
 */
public class MessageDecodingService {

    private final Map<Integer, DecodingStrategy> strategies;

    public MessageDecodingService() {
        this.strategies = new HashMap<>();

        AsciiDecodingStrategy asciiStrategy = new AsciiDecodingStrategy();
        HexDecodingStrategy hexStrategy = new HexDecodingStrategy();

        strategies.put(asciiStrategy.getEncodingType(), asciiStrategy);
        strategies.put(hexStrategy.getEncodingType(), hexStrategy);

        System.out.println("Initialized decoding service with " + strategies.size() + " strategies: " + strategies.keySet());
    }

    public String decode(String content, Integer encoding) {
        if (encoding == null) {
            throw new IllegalArgumentException("Encoding type cannot be null");
        }

        DecodingStrategy strategy = strategies.get(encoding);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported encoding type: " + encoding);
        }

        return strategy.decode(content);
    }


}
