package org.example.orderservice.order.kafka;


import org.example.orderservice.order.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShippingEventListener {

    private final OrderService orderService;

    public ShippingEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "shipping.status.updated", groupId = "shipping-service")
    public void handleOrderCreatedEvent(ShippingStatusUpdateEvent event) {

        // Update the order status based on the shipping status
        orderService.updateOrderStatus(event.orderId(), event.shippingStatus());
    }
}