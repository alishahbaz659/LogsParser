package com.example.LogsParser.strategy;

/**
 * Strategy interface for decoding message content
 */
public interface DecodingStrategy {

    String decode(String content);

    int getEncodingType();

}
