package com.piligrimchick.bot.application.message_processing;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProcessingStrategy {
    boolean supports(Message message);
    void processMessage(Message message);
}
