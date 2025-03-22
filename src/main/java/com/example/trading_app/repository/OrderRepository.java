package com.example.trading_app.repository;

import com.example.trading_app.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> finfByUserId(Long id);
}
