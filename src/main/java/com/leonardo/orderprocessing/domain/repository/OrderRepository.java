package com.leonardo.orderprocessing.domain.repository;

import com.leonardo.orderprocessing.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);
}
