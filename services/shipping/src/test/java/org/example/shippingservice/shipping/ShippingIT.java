package org.example.shippingservice.shipping;

import org.example.shippingservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShippingIT {


    @LocalServerPort
    private int port;

    private RestClient restClient;
    @Autowired
    private ShippingRepository shippingRepository;
    private Shipping savedShipping;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/shipping";
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl(getBaseUrl())
                .build();

        Shipping shipping = Shipping.builder()
                .orderId(UUID.randomUUID().toString())
                .itemName("user456")
                .shippingStatus(ShippingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        savedShipping = shippingRepository.save(shipping);
    }

    @Test
    void shouldUpdateShippingStatus() {
        // given
        var shippingUpdateRequestDTO = new ShippingUpdateRequestDTO(
                ShippingStatus.SHIPPED
        );

        // when
        var updatedShipping = restClient.patch()
                .uri("/" + savedShipping.getId() + "/status")
                .body(shippingUpdateRequestDTO)
                .retrieve()
                .body(ShippingResponseDTO.class);

        // then
        assertThat(updatedShipping).isNotNull();
        assertThat(updatedShipping.shippingStatus()).isEqualTo(shippingUpdateRequestDTO.status());
    }
}
