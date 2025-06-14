package org.example.shippingservice.shipping;

import jakarta.validation.constraints.NotNull;

public record ShippingUpdateRequestDTO(
        @NotNull(message = "shipping status is required")
        ShippingStatus status
) {
}
