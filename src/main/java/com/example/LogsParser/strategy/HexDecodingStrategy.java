package com.example.LogsParser.strategy;

/**
 * Strategy for Hexadecimal decoding (encoding = 1)
 */
public class HexDecodingStrategy implements DecodingStrategy {

    @Override
    public String decode(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }

        try {
            return hexToAscii(content.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid hexadecimal content:" + content, e);
        }

    }

    @Override
    public int getEncodingType() {
        return 1;
    }

    private String hexToAscii(String hex) {
        hex = hex.replaceAll("\\s", "");

        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length: " + hex);
        }

        StringBuilder ascii = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String hexPair = hex.substring(i, i + 2);
            int charCode = Integer.parseInt(hexPair, 16);
            ascii.append((char) charCode);
        }

        return ascii.toString();
    }
}
