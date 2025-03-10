package com.piligrimchick.bot.infrastructure;

import com.piligrimchick.bot.config.BotConfig;
import com.piligrimchick.bot.application.TelegramMessageHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotAdapter extends TelegramLongPollingBot {
    private final String botUsername;
    private final TelegramMessageHandler messageHandler;

    public TelegramBotAdapter(BotConfig botConfig, TelegramMessageHandler messageHandler) {
        super(botConfig.getToken());
        this.botUsername = botConfig.getUsername();
        this.messageHandler = messageHandler;

        System.out.println("TelegramBotAdapter: token loaded!");
    }

    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageHandler.handleMessage(update.getMessage());
        }
    }

    @EventListener
    public void registerBot(org.springframework.boot.context.event.ApplicationReadyEvent event) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("TelegramBotAdapter: Bot registered successfully!");
        } catch (TelegramApiException e) {
            System.out.println("TelegramBotAdapter: Failed to register bot!");
            e.printStackTrace();
        }
    }
}
