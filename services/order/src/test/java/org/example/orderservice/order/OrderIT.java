package org.example.orderservice.order;

import org.example.orderservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
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
    void shouldCreateOrder() {
        // given
        var item = "laptop";
        var quantity = 2;

        // when
        OrderResponseDTO response = createAndSaveOrder(item, quantity);

        // then
        Objects.requireNonNull(response);
        assertThat(response.id()).isNotNull();
        assertThat(response.item()).isEqualTo(item);
        assertThat(response.quantity()).isEqualTo(quantity);
    }

    @Test
    void shouldNotCreateOrderWithInvalidData() {

        // given & then
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> createAndSaveOrder(null, -1)
        );

        // when
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getResponseBodyAsString().contains("Invalid Model")).isTrue();
        assertThat(exception.getResponseBodyAsString()
                .contains("Item cannot be null")).isTrue();
        assertThat(exception.getResponseBodyAsString()
                .contains("Quantity must be a positive number")).isTrue();
    }

    @Test
    void shouldGetOrder() {
        // given
        var item = "laptop";
        var quantity = 2;
        OrderResponseDTO orderResponseDTO = createAndSaveOrder(item, quantity);

        // when
        OrderResponseDTO response = restClient.get()
                .uri("/{id}", orderResponseDTO.id())
                .retrieve()
                .body(OrderResponseDTO.class);

        // then
        Objects.requireNonNull(response);
        assertThat(response.id()).isEqualTo(orderResponseDTO.id());
        assertThat(response.item()).isEqualTo(orderResponseDTO.item());
        assertThat(response.quantity()).isEqualTo(orderResponseDTO.quantity());
    }

    @Test
    @Disabled
    void shouldBlockRequestOverLimit() {

        // given
        OrderResponseDTO orderResponseDTO = createAndSaveOrder("laptop", 2);

        for (int i = 0; i < 20; i++) {
            var response =restClient.get()
                    .uri("/{id}", orderResponseDTO.id())
                    .retrieve()
                    .body(OrderResponseDTO.class);
            assertThat(response.id()).isEqualTo(orderResponseDTO.id());
        }
    }

    private OrderCreateRequestDTO createOrder(String item, int quantity) {
        return new OrderCreateRequestDTO(item, quantity);
    }

    private OrderResponseDTO createAndSaveOrder(String item, int quantity) {
        OrderCreateRequestDTO orderCreateRequestDTO = createOrder(item, quantity);
        return restClient.post()
                .body(orderCreateRequestDTO)
                .retrieve()
                .body(OrderResponseDTO.class);
    }

}
