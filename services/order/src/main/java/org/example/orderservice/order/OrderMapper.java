package org.example.orderservice.order;

public class OrderMapper {

    public static OrderResponseDTO toOrderResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getItemName(),
                order.getQuantity()
        );
    }



    public static Order toOrder(OrderCreateDTO orderCreateDTO) {
        return new Order(orderCreateDTO.getItem(), orderCreateDTO.getQuantity());
    }
}
