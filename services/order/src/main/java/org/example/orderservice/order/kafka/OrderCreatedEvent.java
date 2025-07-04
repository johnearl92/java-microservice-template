package org.example.orderservice.order.kafka;

import java.util.UUID;

public record OrderCreatedEvent (
    String externalId,
    String traceId,
    String itemName,
    Integer quantity
) {

}
