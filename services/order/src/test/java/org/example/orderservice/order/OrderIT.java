package org.example.orderservice.order;

import org.example.orderservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIT {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/orders";
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl(getBaseUrl())
                .build();
    }

    @Test
    @Order(1)
    void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = createOrder();
        OrderResponseDTO response = restClient.post()
                .body(orderCreateDTO)
                .retrieve()
                .body(OrderResponseDTO.class);

        Objects.requireNonNull(response);
        assertThat(response.id()).isNotNull();
        assertThat(response.item()).isEqualTo(orderCreateDTO.getItem());
        assertThat(response.quantity()).isEqualTo(orderCreateDTO.getQuantity());
    }

    private OrderCreateDTO createOrder() {
        return new OrderCreateDTO("Latte",2);
    }


}
