package org.example.shippingservice.shipping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShippingResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody ShippingUpdateRequestDTO request
    ) {
        return ResponseEntity.ok(shippingService.updateShippingStatus(id, request.status()));
    }
}
