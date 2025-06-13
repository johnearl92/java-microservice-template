package org.example.orderservice.order;

import org.example.orderservice.error.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final Logger logger= LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        logger.info("Creating new order {}", orderCreateRequestDTO);
        var order = this.orderRepository.save(OrderMapper.toOrder(orderCreateRequestDTO));

        return OrderMapper.toOrderResponseDTO(order);
    }

    public OrderResponseDTO getOrderById(Long id) {
        logger.info("Finding order by id {}",id);
        return this.orderRepository.findById(id)
                .map(OrderMapper::toOrderResponseDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}
