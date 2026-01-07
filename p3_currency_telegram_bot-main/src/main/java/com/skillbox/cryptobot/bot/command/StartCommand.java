package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;


@Service
@Slf4j
@AllArgsConstructor
public class StartCommand implements IBotCommand {
    private final SubscriberRepository subscriberRepo;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Начало работы с ботом.";
    }

    @SneakyThrows
    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramId = message.getFrom().getId();

        if (!subscriberRepo.existsByTelegramId(telegramId)) {
            double targetPrice = 0;
            subscriberRepo.save(new Subscriber(telegramId));
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("Добро пожаловать в крипто-бот! Доступные команды:\n/get_price - Получить текущую цену биткоина\n/subscribe - Подписаться на уведомления о цене\n/unsubscribe - Отменить подписку");

        absSender.execute(answer);
    }
}
