
package com.ejahijagic.auth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class RabbitNotificationPublisher implements NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitNotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishSms(String phoneNumber, String message) {
        Map<String, String> payload = Map.of("channel", "SMS", "phoneNumber", phoneNumber, "content", message);
        rabbitTemplate.convertAndSend("notification.exchange", "notification.sms", payload);
    }
}
