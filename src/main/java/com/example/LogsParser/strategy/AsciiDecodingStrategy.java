package com.example.LogsParser.strategy;

/**
 * Strategy for ASCII decoding (encoding = 0)
 */
public class AsciiDecodingStrategy implements DecodingStrategy {

    @Override
    public String decode(String content) {
        return content != null ? content.trim() : "";
    }

    @Override
    public int getEncodingType() {
        return 0;
    }

}
