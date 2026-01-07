package com.skillbox.cryptobot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriberRepository subscriberRepository;

    public void subscribeUser(Long telegramId, Double targetPrice) {
        Subscriber subscriber = subscriberRepository.findByTelegramId(telegramId)
                .orElseGet(() -> new Subscriber(telegramId));

        subscriber.setTargetPrice(targetPrice);
        subscriberRepository.save(subscriber);
    }

    public void unsubscribeUser(Long telegramId) {
        Subscriber subscriber = subscriberRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        subscriber.setTargetPrice(null);
        subscriberRepository.save(subscriber);
    }

    public boolean hasActiveSubscription(Long telegramId) {
        return subscriberRepository.existsByTelegramIdAndDesiredPriceIsNotNull(telegramId);
    }
}