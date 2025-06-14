package org.example.shippingservice.shipping;

public record ShippingResponseDTO(
        Long id,
        String orderId,
        String itemName,
        ShippingStatus shippingStatus
) {

}
