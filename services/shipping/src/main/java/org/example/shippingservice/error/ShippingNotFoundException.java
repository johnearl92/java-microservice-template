package org.example.shippingservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ShippingNotFoundException extends RuntimeException {

    public ShippingNotFoundException(String message) {
        super(message);
    }
}
