package org.example.orderservice.order;

public record OrderResponseDTO(
        Long id,
        String item,
        int quantity
) {
}
