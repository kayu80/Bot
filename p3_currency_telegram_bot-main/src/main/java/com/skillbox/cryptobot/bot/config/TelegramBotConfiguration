package com.skillbox.cryptobot.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.skillbox.cryptobot.bot.CryptoBot;

@Configuration
@Slf4j
public class TelegramBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(CryptoBot cryptoBot) {
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(cryptoBot);
        } catch (TelegramApiException e) {
            log.error("Ошибка регистрации бота в Telegram!", e);
        }
        return botsApi;
    }
}
