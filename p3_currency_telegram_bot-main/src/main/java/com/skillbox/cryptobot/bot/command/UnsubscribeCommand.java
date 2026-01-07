package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {
    private final SubscriberRepository subscriberRepo;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменить подписку на уведомления о цене.";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramId = message.getFrom().getId();

        Optional<Subscriber> existingSubscription = subscriberRepo.findByTelegramId(telegramId);

        if (existingSubscription.isPresent()) {
            Subscriber subscriber = existingSubscription.get();
            subscriber.setDesiredPrice(null);
            subscriberRepo.save(subscriber);
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("Подписка успешно отменена.");
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
