package com.retoBTG.Subscription_Service.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service", url = "${notification-service.url}")
public interface NotificationClient {

    @PostMapping("/notifications/email")
    void sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body);

    @PostMapping("/notifications/sms")
    void sendSms(@RequestParam String to, @RequestParam String body);
}
