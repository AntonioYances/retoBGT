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

@ExtendWith(SpringExtension.class)  // Usar el soporte de Spring para pruebas
@SpringBootTest
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;  // El servicio que estamos probando

    @MockBean
    private SubscriptionRepository subscriptionRepository;  // Repositorio simulado

    @MockBean
    private NotificationClient notificationClient;  // Cliente de notificación simulado

    private Subscription subscription;

    @BeforeEach
    public void setUp() {
        // Inicializa los objetos necesarios para las pruebas
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
        // Simulamos el comportamiento del repositorio
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Llamamos al método a probar
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);

        // Verificamos si el servicio llamó a los métodos del repositorio y del cliente de notificación
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(subscription.getClientEmail()), eq("New Subscription"), any(String.class));
        verify(notificationClient).sendSms(eq(subscription.getClientPhone()), any(String.class));

        // Aseguramos que la suscripción devuelta es la correcta
        assertThat(createdSubscription).isEqualTo(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        // Configuramos el comportamiento del repositorio para encontrar una suscripción existente
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Nueva suscripción con detalles actualizados
        Subscription updatedDetails = new Subscription();
        updatedDetails.setClientName("Jane Doe");
        updatedDetails.setFundName("Growth Fund");
        updatedDetails.setClientEmail("jane.doe@example.com");
        updatedDetails.setClientPhone("987654321");
        updatedDetails.setActive(true);

        // Llamamos al método de actualización
        Subscription updatedSubscription = subscriptionService.updateSubscription(1L, updatedDetails);

        // Verificamos que el repositorio y las notificaciones sean llamadas correctamente
        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(updatedDetails.getClientEmail()), eq("Subscription Updated"), any(String.class));
        verify(notificationClient).sendSms(eq(updatedDetails.getClientPhone()), any(String.class));

        // Aseguramos que la suscripción fue actualizada correctamente
        assertThat(updatedSubscription.getClientName()).isEqualTo("Jane Doe");
        assertThat(updatedSubscription.getFundName()).isEqualTo("Growth Fund");
    }

    @Test
    public void testDeleteSubscription() {
        // Configuramos el comportamiento del repositorio para encontrar una suscripción existente
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        // Llamamos al método de eliminación
        subscriptionService.deleteSubscription(1L);

        // Verificamos que la suscripción fue eliminada del repositorio
        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository).delete(any(Subscription.class));
        verify(notificationClient).sendEmail(eq(subscription.getClientEmail()), eq("Subscription Canceled"), any(String.class));
        verify(notificationClient).sendSms(eq(subscription.getClientPhone()), any(String.class));
    }

    @Test
    public void testGetAllSubscriptions() {
        // Configuramos el comportamiento del repositorio para devolver una lista de suscripciones
        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));

        // Llamamos al método que obtiene todas las suscripciones
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();

        // Verificamos que la lista de suscripciones no esté vacía
        assertThat(subscriptions).isNotEmpty();
        assertThat(subscriptions.get(0)).isEqualTo(subscription);
    }

    @Test
    public void testGetSubscriptionById() {
        // Configuramos el comportamiento del repositorio para devolver una suscripción por ID
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        // Llamamos al método para obtener la suscripción por ID
        Optional<Subscription> foundSubscription = subscriptionService.getSubscriptionById(1L);

        // Verificamos que la suscripción fue encontrada
        assertThat(foundSubscription).isPresent();
        assertThat(foundSubscription.get()).isEqualTo(subscription);
    }
}
