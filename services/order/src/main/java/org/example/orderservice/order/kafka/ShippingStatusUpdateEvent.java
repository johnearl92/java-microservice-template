package org.example.orderservice.order.kafka;


public record ShippingStatusUpdateEvent(
        String orderId,
        String shippingStatus
) {
}
