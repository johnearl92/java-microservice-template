package org.example.shippingservice.kafka;


public record ShippingStatusUpdateEvent(
        String orderId,
        String shippingStatus
) {
}
