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

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {
    private final SubscriberRepository subscriberRepo;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписаться на уведомления о достижении целевой цены.";
    }

    @SneakyThrows
    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramId = message.getFrom().getId();

        if (arguments.length != 1 || !isValidNumber(arguments[0])) {
            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());
            answer.setText("Некорректный формат команды. Пример: /subscribe 34600");
            absSender.execute(answer);
            return;
        }

        double targetPrice = Double.parseDouble(arguments[0]);

        Optional<Subscriber> existingSubscription = subscriberRepo.findByTelegramId(telegramId);

        if (existingSubscription.isPresent()) {
            Subscriber subscriber = existingSubscription.get();
            subscriber.setDesiredPrice(targetPrice);
            subscriberRepo.save(subscriber);
        } else {
            Subscriber newSubscriber = new Subscriber(telegramId);
            subscriberRepo.save(newSubscriber);
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("Подписка успешно создана на цену: $" + targetPrice);
        absSender.execute(answer);
    }

    private boolean isValidNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
