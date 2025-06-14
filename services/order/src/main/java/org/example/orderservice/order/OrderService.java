package org.example.orderservice.order;

import org.example.orderservice.error.OrderNotFoundException;
import org.example.orderservice.order.kafka.OrderEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final Logger logger= LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;


    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO orderCreateRequestDTO) {
        logger.info("Creating new order {}", orderCreateRequestDTO);
        var order = this.orderRepository.save(OrderMapper.toOrder(orderCreateRequestDTO));
        orderEventProducer.publishOrderCreatedEvent(OrderMapper.toOrderCreatedEvent(order));
        return OrderMapper.toOrderResponseDTO(order);
    }

    public OrderResponseDTO getOrderById(Long id) {
        logger.info("Finding order by id {}",id);
        return this.orderRepository.findById(id)
                .map(OrderMapper::toOrderResponseDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public List<OrderResponseDTO> getAllOrders() {
        logger.info("Fetching all orders");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderMapper::toOrderResponseDTO)
                .toList();
    }
}
