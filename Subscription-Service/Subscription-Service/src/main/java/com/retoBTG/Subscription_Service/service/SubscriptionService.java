package com.retoBTG.Subscription_Service.service;

import com.retoBTG.Subscription_Service.config.NotificationClient;
import com.retoBTG.Subscription_Service.entity.Subscription;
import com.retoBTG.Subscription_Service.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private NotificationClient notificationClient;
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    
    public Subscription createSubscription(Subscription subscription) {
        // Guardar la nueva suscripción en la base de datos
        Subscription createdSubscription = subscriptionRepository.save(subscription);

        
        notificationClient.sendEmail(subscription.getClientEmail(),
                "New Subscription",
                "Thank you for subscribing to " + subscription.getFundName());

        notificationClient.sendSms(subscription.getClientPhone(),
                "Thank you for subscribing to " + subscription.getFundName());

        
        return createdSubscription;
    }

    
    public Subscription updateSubscription(Long id, Subscription subscriptionDetails) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setClientName(subscriptionDetails.getClientName());
        subscription.setFundName(subscriptionDetails.getFundName());
        subscription.setActive(subscriptionDetails.isActive());
        subscription.setClientEmail(subscriptionDetails.getClientEmail());
        subscription.setClientPhone(subscriptionDetails.getClientPhone());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        notificationClient.sendEmail(subscription.getClientEmail(),
                "Subscription Updated",
                "Your subscription to " + subscription.getFundName() + " has been updated.");

        notificationClient.sendSms(subscription.getClientPhone(),
                "Your subscription to " + subscription.getFundName() + " has been updated.");

        return updatedSubscription;
    }

    
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscriptionRepository.delete(subscription);

        notificationClient.sendEmail(subscription.getClientEmail(),
                "Subscription Canceled",
                "Your subscription to " + subscription.getFundName() + " has been canceled.");

        notificationClient.sendSms(subscription.getClientPhone(),
                "Your subscription to " + subscription.getFundName() + " has been canceled.");
    }
}
