package com.example.trading_app.repository;

import com.example.trading_app.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository <OrderItem,Long> {
}
