package org.example.orderservice.order;

public class OrderCreateDTO {
    private String item;
    private int quantity;

    public OrderCreateDTO() {
    }

    public OrderCreateDTO(String item, int quantity) {
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
