package com.example.trading_app.repository;

import com.example.trading_app.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository <OrderItem,Long> {
}
