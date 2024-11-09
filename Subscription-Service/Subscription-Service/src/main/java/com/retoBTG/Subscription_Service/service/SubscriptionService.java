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

    /**
     * Crear una nueva suscripción y enviar notificación.
     *
     * @param subscription La nueva suscripción.
     * @return La suscripción creada.
     */
    public Subscription createSubscription(Subscription subscription) {
        // Guardar la nueva suscripción en la base de datos
        Subscription createdSubscription = subscriptionRepository.save(subscription);

        // Enviar notificación por correo electrónico al cliente
        notificationClient.sendEmail(subscription.getClientEmail(),
                "New Subscription",
                "Thank you for subscribing to " + subscription.getFundName());

        // Enviar notificación por SMS al cliente
        notificationClient.sendSms(subscription.getClientPhone(),
                "Thank you for subscribing to " + subscription.getFundName());

        // Retornar la suscripción creada
        return createdSubscription;
    }

    /**
     * Actualizar una suscripción existente y enviar notificación.
     *
     * @param id El ID de la suscripción a actualizar.
     * @param subscriptionDetails Los nuevos detalles de la suscripción.
     * @return La suscripción actualizada.
     */
    public Subscription updateSubscription(Long id, Subscription subscriptionDetails) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setClientName(subscriptionDetails.getClientName());
        subscription.setFundName(subscriptionDetails.getFundName());
        subscription.setActive(subscriptionDetails.isActive());
        subscription.setClientEmail(subscriptionDetails.getClientEmail());
        subscription.setClientPhone(subscriptionDetails.getClientPhone());

        // Guardar la suscripción actualizada
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        // Enviar una notificación al cliente sobre la actualización
        notificationClient.sendEmail(subscription.getClientEmail(),
                "Subscription Updated",
                "Your subscription to " + subscription.getFundName() + " has been updated.");

        notificationClient.sendSms(subscription.getClientPhone(),
                "Your subscription to " + subscription.getFundName() + " has been updated.");

        return updatedSubscription;
    }

    /**
     * Eliminar una suscripción y enviar notificación.
     *
     * @param id El ID de la suscripción a eliminar.
     */
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        // Eliminar la suscripción
        subscriptionRepository.delete(subscription);

        // Enviar notificación por correo electrónico al cliente
        notificationClient.sendEmail(subscription.getClientEmail(),
                "Subscription Canceled",
                "Your subscription to " + subscription.getFundName() + " has been canceled.");

        // Enviar notificación por SMS al cliente
        notificationClient.sendSms(subscription.getClientPhone(),
                "Your subscription to " + subscription.getFundName() + " has been canceled.");
    }
}
