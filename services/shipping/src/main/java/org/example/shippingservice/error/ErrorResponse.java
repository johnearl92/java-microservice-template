package org.example.shippingservice.error;


import java.util.List;

public record ErrorResponse(
    String title,
    Integer status,
    List<String> errors
) {
}
