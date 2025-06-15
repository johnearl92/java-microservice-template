package org.example.orderservice.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByExternalId(String externalId);


    // For createdAt cursor
    List<Order> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime cursor, Pageable pageable);

    // For first page (no cursor)
    List<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
