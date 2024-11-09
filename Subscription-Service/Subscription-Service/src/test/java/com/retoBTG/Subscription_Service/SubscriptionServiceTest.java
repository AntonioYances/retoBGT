package com.retoBTG.Subscription_Service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


import com.retoBTG.Subscription_Service.config.NotificationClient;
import com.retoBTG.Subscription_Service.entity.Subscription;
import com.retoBTG.Subscription_Service.repository.SubscriptionRepository;
import com.retoBTG.Subscription_Service.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)  
@SpringBootTest
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;  
    @MockBean
    private SubscriptionRepository subscriptionRepository;  

    @MockBean
    private NotificationClient notificationClient;  

    private Subscription subscription;

    @BeforeEach
    public void setUp() {
        
        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setClientName("John Doe");
        subscription.setFundName("Tech Fund");
        subscription.setClientEmail("john.doe@example.com");
        subscription.setClientPhone("123456789");
        subscription.setActive(true);
    }

    @Test
    public void testCreateSubscription() {
        
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);

        verify(subscriptionRepository).save(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(subscription.getClientEmail()), eq("New Subscription"), any(String.class));
        verify(notificationClient).sendSms(eq(subscription.getClientPhone()), any(String.class));

        assertThat(createdSubscription).isEqualTo(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription updatedDetails = new Subscription();
        updatedDetails.setClientName("Jane Doe");
        updatedDetails.setFundName("Growth Fund");
        updatedDetails.setClientEmail("jane.doe@example.com");
        updatedDetails.setClientPhone("987654321");
        updatedDetails.setActive(true);

        Subscription updatedSubscription = subscriptionService.updateSubscription(1L, updatedDetails);

        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(updatedDetails.getClientEmail()), eq("Subscription Updated"), any(String.class));
        verify(notificationClient).sendSms(eq(updatedDetails.getClientPhone()), any(String.class));

        assertThat(updatedSubscription.getClientName()).isEqualTo("Jane Doe");
        assertThat(updatedSubscription.getFundName()).isEqualTo("Growth Fund");
    }

    @Test
    public void testDeleteSubscription() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        subscriptionService.deleteSubscription(1L);

        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository).delete(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(subscription.getClientEmail()), eq("Subscription Canceled"), any(String.class));
        verify(notificationClient).sendSms(eq(subscription.getClientPhone()), any(String.class));
    }

    @Test
    public void testGetAllSubscriptions() {
        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));

        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();

        assertThat(subscriptions).isNotEmpty();
        assertThat(subscriptions.get(0)).isEqualTo(subscription);
    }

    @Test
    public void testGetSubscriptionById() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Optional<Subscription> foundSubscription = subscriptionService.getSubscriptionById(1L);

        assertThat(foundSubscription).isPresent();
        assertThat(foundSubscription.get()).isEqualTo(subscription);
    }
}
