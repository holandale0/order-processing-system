package com.leonardo.orderprocessing.infrastructure.persistence;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    @Override
    public Order save(Order order) {
        boolean exists = jpaRepository.existsById(order.getId());
        OrderEntity entity = exists ? mapper.toEntity(order) : mapper.toNewEntity(order);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}
