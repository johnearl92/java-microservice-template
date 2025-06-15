package org.example.orderservice.order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record PaginatedOrders(List<Order> orders, LocalDateTime nextCursor) {}

