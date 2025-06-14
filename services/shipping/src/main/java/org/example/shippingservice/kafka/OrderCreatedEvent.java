package org.example.shippingservice.kafka;

import java.util.UUID;

public record OrderCreatedEvent(
    String externalId,
    String traceId,
    String itemName,
    Integer quantity
) {

}
