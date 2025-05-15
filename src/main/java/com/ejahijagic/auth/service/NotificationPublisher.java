
package com.ejahijagic.auth.service;

public interface NotificationPublisher {
    void publishSms(String phoneNumber, String message);
}
