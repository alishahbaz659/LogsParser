package com.example.LogsParser.service;

import com.example.LogsParser.model.LogMessage;
import com.example.LogsParser.model.Pipeline;
import com.example.LogsParser.util.LogMessageUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for reconstructing pipelines from grouped log messages in reverse order
 */
public class PipelineReconstructionService {

    public List<Pipeline> reconstructPipelines(Map<String, List<LogMessage>> messagesByPipeline) {
        List<Pipeline> pipelines = new ArrayList<>();

        for (Map.Entry<String, List<LogMessage>> entry : messagesByPipeline.entrySet()) {
            String pipelineId = entry.getKey();
            List<LogMessage> messages = entry.getValue();

            try {
                Pipeline pipeline = reconstructSinglePipeline(pipelineId, messages);
                pipelines.add(pipeline);

            } catch (Exception e) {
                System.out.println("Warning: Failed to reconstruct pipeline " + pipelineId + ": " + e.getMessage());
                // Still add the pipeline with available messages
                Pipeline pipeline = new Pipeline(pipelineId);
                pipeline.setMessages(messages);
                pipelines.add(pipeline);
            }
        }

        pipelines.sort(Comparator.comparing(Pipeline::getId).reversed());
        System.out.println("Reconstructed " + pipelines.size() + " pipelines");
        return pipelines;
    }

    private Pipeline reconstructSinglePipeline(String pipelineId, List<LogMessage> messages) {
        Pipeline pipeline = new Pipeline(pipelineId);

        if (messages.isEmpty()) {
            System.out.println("Warning: Pipeline " + pipelineId + " has no messages");
            return pipeline;
        }

        List<LogMessage> reverseOrderedMessages;

        // for O(1) access
        Map<String, LogMessage> messageMap = messages.stream()
                .collect(Collectors.toMap(LogMessage::getId, msg -> msg));


        Optional<LogMessage> chainTerminator = messages.stream()
                .filter(LogMessageUtil::isLastMessage)
                .findFirst();

        // Strategy 1: Try to find chain terminator (next_id = "-1")
        if (chainTerminator.isPresent()) {
            reverseOrderedMessages = buildReverseChainFromTail(chainTerminator.get(), messageMap);
            System.out.println("Pipeline " + pipelineId + ": Built chain from terminator " + chainTerminator.get().getId());
        } else {
            // Strategy 2: Find orphan messages (not referenced by other nodes)
            Set<String> referencedIds = messages.stream()
                    .filter(LogMessageUtil::hasNext)
                    .map(LogMessage::getNextId)
                    .collect(Collectors.toSet());

            List<LogMessage> orphanMessages = messages.stream()
                    .filter(msg -> !referencedIds.contains(msg.getId()))
                    .collect(Collectors.toList());

            if (orphanMessages.size() == 1) {
                // Build forward chain from orphan (head), then reverse
                reverseOrderedMessages = buildReverseChainFromHead(orphanMessages.get(0), messageMap);
                System.out.println("Pipeline " + pipelineId + ": Built chain from orphan " + orphanMessages.get(0).getId());
            } else {
                // Strategy 3: Fallback to ID-based sorting
                reverseOrderedMessages = messages.stream()
                        .sorted(Comparator.comparing(LogMessage::getId))
                        .collect(Collectors.toList());
                System.out.println("Pipeline " + pipelineId + ": Used fallback ordering (sorted by ID)");
            }
        }
        pipeline.setMessages(reverseOrderedMessages);
        return pipeline;
    }


    /**
     * Build reverse-ordered chain by traversing backwards from tail message
     *
     * @param tailMessage The message to start backward traversal from
     * @param messageMap  Map for fast message lookup by ID
     * @return List of messages in reverse order (tail-first)
     */
    private List<LogMessage> buildReverseChainFromTail(LogMessage tailMessage, Map<String, LogMessage> messageMap) {
        List<LogMessage> reverseChain = new ArrayList<>();
        Set<String> visitedIds = new HashSet<>();

        LogMessage currentNode = tailMessage;

        // traverse backwards: tail -> ... -> head
        while (currentNode != null && !visitedIds.contains(currentNode.getId())) {
            visitedIds.add(currentNode.getId());
            reverseChain.add(currentNode);

            currentNode = findPredecessor(currentNode, messageMap);
        }

        return reverseChain;
    }

    private LogMessage findPredecessor(LogMessage targetMessage, Map<String, LogMessage> messageMap) {
        return messageMap.values().stream()
                .filter(msg -> LogMessageUtil.pointsTo(msg, targetMessage))
                .findFirst()
                .orElse(null);
    }

    /**
     * Build reverse-ordered chain by traversing forward from head message, then reversing
     *
     * @param headMessage The message to start forward traversal from (Orphan: No node is pointing to it)
     * @param messageMap  Map for fast message lookup by ID
     * @return List of messages in reverse order (tail-first)
     */
    private List<LogMessage> buildReverseChainFromHead(LogMessage headMessage, Map<String, LogMessage> messageMap) {
        List<LogMessage> forwardChain = new ArrayList<>();
        Set<String> visitedIds = new HashSet<>();

        LogMessage currentNode = headMessage;

        // Traverse forward: head -> ... -> tail
        while (currentNode != null && !visitedIds.contains(currentNode.getId())) {
            visitedIds.add(currentNode.getId());
            forwardChain.add(currentNode);

            if (LogMessageUtil.hasNext(currentNode)) {
                currentNode = messageMap.get(currentNode.getNextId());
            } else {
                currentNode = null;
            }
        }
        Collections.reverse(forwardChain);
        return forwardChain;
    }

}