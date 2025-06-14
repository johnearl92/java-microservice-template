package org.example.shippingservice.kafka;

import org.example.shippingservice.shipping.ShippingStatus;

public record ShippingStatusUpdateEvent(
        String orderId,
        ShippingStatus shippingStatus
) {
}
