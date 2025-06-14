package org.example.shippingservice.shipping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
