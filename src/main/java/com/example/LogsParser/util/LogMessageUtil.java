package com.example.LogsParser.util;

import com.example.LogsParser.model.LogMessage;

/**
 * Utility class for LogMessage business logic
 * containing helper methods
 */
public final class LogMessageUtil {

    private LogMessageUtil() {
    }

    public static boolean isLastMessage(LogMessage message) {
        return message != null && "-1".equals(message.getNextId());
    }

    public static boolean hasNext(LogMessage message) {
        return message != null &&
                message.getNextId() != null &&
                !"-1".equals(message.getNextId());
    }

    /**
     * Check if a message points to the target message (is its predecessor)
     *
     * @param message       The potential predecessor message
     * @param targetMessage The target message to check against
     * @return true if a message points to targetMessage
     */
    public static boolean pointsTo(LogMessage message, LogMessage targetMessage) {
        return message != null &&
                targetMessage != null &&
                targetMessage.getId().equals(message.getNextId());
    }

}
