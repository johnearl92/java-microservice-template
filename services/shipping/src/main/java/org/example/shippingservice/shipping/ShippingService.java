package org.example.shippingservice.shipping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shippingservice.error.ShippingNotFoundException;
import org.example.shippingservice.kafka.OrderCreatedEvent;
import org.example.shippingservice.kafka.ShippingStatusUpdateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final KafkaTemplate<String, ShippingStatusUpdateEvent> kafkaTemplate;

    public void createShippingFromOrder(OrderCreatedEvent event) {
        log.info("Creating shipping for order {}", event.externalId());
        Shipping shipping = Shipping.builder()
                .orderId(event.externalId())
                .itemName(event.itemName())
                .shippingStatus(ShippingStatus.PENDING)
                .build();

        shippingRepository.save(shipping);

        log.info("Shipping saved for order {}", event.externalId());
    }

    @Transactional
    public ShippingResponseDTO updateShippingStatus(Long id, ShippingStatus status) {
        return shippingRepository
                .findById(id)
                .map(shippingEntity -> {
                    shippingEntity.setShippingStatus(status);
                    kafkaTemplate.send("shipping.status.updated",
                            new ShippingStatusUpdateEvent(shippingEntity.getOrderId(), status));
                    return ShippingMapper.toShippingResponseDTO(shippingEntity);
                })
                .orElseThrow(() -> new ShippingNotFoundException("Shipping not found for order: " + id));
    }
}
