package org.example.shippingservice.shipping;

import org.example.shippingservice.kafka.OrderCreatedEvent;
import org.example.shippingservice.kafka.ShippingStatusUpdateEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShippingServiceTest {

    private final ShippingRepository shippingRepository = mock(ShippingRepository.class);
    private final KafkaTemplate<String, ShippingStatusUpdateEvent> kafkaTemplate = mock(KafkaTemplate.class);

    private final ShippingService shippingService = new ShippingService(shippingRepository, kafkaTemplate);


    @Test
    void handleOrderCreatedEvent_shouldSaveShippingEntity() {
        // Given
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "product-name",
                2);

        // When
        shippingService.createShippingFromOrder(event);

        // Then
        ArgumentCaptor<Shipping> captor = ArgumentCaptor.forClass(Shipping.class);
        verify(shippingRepository, times(1)).save(captor.capture());

        Shipping savedShipping = captor.getValue();

        assertThat(savedShipping.getOrderId()).isNotNull();
        assertThat(savedShipping.getOrderId()).isEqualTo(event.externalId());
        assertThat(savedShipping.getItemName()).isEqualTo(event.itemName());
        assertThat(savedShipping.getShippingStatus()).isEqualTo(ShippingStatus.PENDING);
    }

    @Test
    void updateShippingStatus_shouldPublishKafkaEvent() {
        // Given
        Shipping shipping = Shipping.builder()
                .orderId(UUID.randomUUID().toString())
                .itemName("user456")
                .shippingStatus(ShippingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(shippingRepository.findById(shipping.getId())).thenReturn(Optional.of(shipping));

        // When
        shippingService.updateShippingStatus(shipping.getId(), ShippingStatus.DELIVERED);

        // Then
        ArgumentCaptor<ShippingStatusUpdateEvent> captor = ArgumentCaptor.forClass(ShippingStatusUpdateEvent.class);
        verify(kafkaTemplate).send(eq("shipping.status.updated"), captor.capture());

        ShippingStatusUpdateEvent event = captor.getValue();
        assertThat(event.orderId()).isEqualTo(shipping.getOrderId());
        assertThat(event.shippingStatus()).isEqualTo(ShippingStatus.DELIVERED);
    }
}