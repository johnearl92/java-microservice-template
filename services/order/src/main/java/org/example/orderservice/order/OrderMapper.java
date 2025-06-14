package org.example.orderservice.order;

import org.example.orderservice.config.MDCLoggingFilter;
import org.example.orderservice.order.kafka.OrderCreatedEvent;
import org.slf4j.MDC;

public class OrderMapper {

    public static OrderResponseDTO toOrderResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getItemName(),
                order.getQuantity(),
                order.getOrderStatus()
        );
    }

    public static Order toOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        return new Order(orderCreateRequestDTO.getItem(), orderCreateRequestDTO.getQuantity());
    }

    public static OrderCreateRequestDTO toOrderRequestDTO(OrderResponseDTO orderResponseDTO) {
        return new OrderCreateRequestDTO(
                orderResponseDTO.item(),
                orderResponseDTO.quantity()
        );
    }

    public static OrderCreatedEvent toOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getExternalId(),
                MDCLoggingFilter.getTraceId(),
                order.getItemName(),
                order.getQuantity()
        );
    }
}
