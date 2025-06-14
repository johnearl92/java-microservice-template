package org.example.shippingservice.shipping;

public class ShippingMapper {
    public static ShippingResponseDTO toShippingResponseDTO(Shipping shipping) {
        return new ShippingResponseDTO(
                shipping.getId(),
                shipping.getOrderId(),
                shipping.getItemName(),
                shipping.getShippingStatus()
        );
    }
}
