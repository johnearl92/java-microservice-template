package org.example.orderservice.order.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderEventProducerTest {

    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private OrderEventProducer orderEventProducer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        orderEventProducer = new OrderEventProducer(kafkaTemplate);
    }

    @Test
    void shouldPublishOrderCreatedEventToKafka() {
        // Given
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                "product-name",
                2);

        // When
        orderEventProducer.publishOrderCreatedEvent(event);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), eventCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("order.created");
        assertThat(eventCaptor.getValue()).isEqualTo(event);
    }
}