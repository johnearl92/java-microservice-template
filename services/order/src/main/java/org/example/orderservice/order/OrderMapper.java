package org.example.orderservice.order;

public class OrderMapper {

    public static OrderResponseDTO toOrderResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getItemName(),
                order.getQuantity()
        );
    }

    public static Order toOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        return new Order(orderCreateRequestDTO.getItem(), orderCreateRequestDTO.getQuantity(), OrderStatus.PENDING);
    }

    public static OrderCreateRequestDTO toOrderRequestDTO(OrderResponseDTO orderResponseDTO) {
        return new OrderCreateRequestDTO(
                orderResponseDTO.item(),
                orderResponseDTO.quantity()
        );
    }
}
