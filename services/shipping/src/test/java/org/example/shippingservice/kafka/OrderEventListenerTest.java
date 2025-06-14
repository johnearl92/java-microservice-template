package org.example.shippingservice.kafka;

import org.example.shippingservice.shipping.ShippingService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderEventListenerTest {

    private final ShippingService shippingService = mock(ShippingService.class);
    private final OrderEventListener listener = new OrderEventListener(shippingService);

    @Test
    void handleOrderCreatedEvent_shouldCallShippingService() {
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "product-name",
                2);

        listener.handleOrderCreatedEvent(event);

        verify(shippingService).createShippingFromOrder(event);
    }
}
