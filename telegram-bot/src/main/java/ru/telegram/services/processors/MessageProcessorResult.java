package ru.telegram.services.processors;

import lombok.Data;

@Data
public class MessageProcessorResult {
    String okReply;
    String failReply;
}
