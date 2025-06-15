package org.example.orderservice.order;

import org.example.orderservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIT {

    @LocalServerPort
    private int port;
    private RestClient restClient;


    @Autowired
    private OrderRepository orderRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/orders";
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl(getBaseUrl())
                .build();
        orderRepository.deleteAll();

        Instant now = Instant.now();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setItemName("Order " + i);
            order.setExternalId(UUID.randomUUID().toString());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.ofInstant(now.minusSeconds(i * 60), ZoneId.systemDefault()));
            orderRepository.save(order);
        }
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
        assertThat(response.orderStatus()).isEqualTo(OrderStatus.PENDING);
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
    void shouldReturnsOrdersAndCursorForFirstPage() {
        PaginatedOrders response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("limit", 5)
                        .build())
                .retrieve()
                .body(PaginatedOrders.class);

        assertThat(response).isNotNull();
        assertThat(response.orders().size()).isEqualTo(5);
        assertThat(response.nextCursor()).isNotNull();
    }

    @Test
    void testSecondPageUsingCursor() {
        // First page
        PaginatedOrders firstPage = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("limit", 5)
                        .build())
                .retrieve()
                .body(PaginatedOrders.class);

        assertThat(firstPage).isNotNull();
        assertThat(firstPage.orders().size()).isEqualTo(5);

        LocalDateTime nextCursor = firstPage.nextCursor();
        assertThat(nextCursor).isNotNull();

        // Second page
        PaginatedOrders secondPage = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("limit", 5)
                        .queryParam("cursor", nextCursor)
                        .build())
                .retrieve()
                .body(PaginatedOrders.class);

        assertThat(secondPage).isNotNull();
        assertThat(secondPage.orders().size()).isEqualTo(5);
        assertThat(secondPage.nextCursor()).isNotNull();
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
