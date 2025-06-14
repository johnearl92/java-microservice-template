package org.example.orderservice.order.kafka;

import java.util.UUID;

public record OrderCreatedEvent (
    UUID externalId,
    String traceId,
    String itemName,
    Integer quantity
) {

}
