package org.example.orderservice.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateDTO orderCreateDTO) {
        // Simulate order creation logic
        var order = this.orderRepository.save(OrderMapper.toOrder(orderCreateDTO));

        return OrderMapper.toOrderResponseDTO(order);
    }
}
