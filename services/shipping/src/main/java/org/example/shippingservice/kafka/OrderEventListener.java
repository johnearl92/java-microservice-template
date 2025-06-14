package org.example.shippingservice.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shippingservice.shipping.Shipping;
import org.example.shippingservice.shipping.ShippingRepository;
import org.example.shippingservice.shipping.ShippingService;
import org.example.shippingservice.shipping.ShippingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final ShippingService shippingService;

    @KafkaListener(topics = "order.created", groupId = "order-service")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        shippingService.createShippingFromOrder(event);
    }
}