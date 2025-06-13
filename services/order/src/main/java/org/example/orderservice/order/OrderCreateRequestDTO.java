package org.example.orderservice.order;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderCreateRequestDTO {

    @NotNull(message = "Item cannot be null")
    private String item;

    @Positive(message = "Quantity must be a positive number")
    private int quantity;

    public OrderCreateRequestDTO() {
    }

    public OrderCreateRequestDTO(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
