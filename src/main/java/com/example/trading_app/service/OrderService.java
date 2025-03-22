package com.example.trading_app.service;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.OrderItem;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.OrderType;

import java.util.List;

public interface OrderService {
    Order createOrder(User user , OrderItem orderItem , OrderType orderType);
    Order getOrderId(Long orderId) throws Exception;
    List<Order> getAllOrdersOfUser(Long userId , OrderType orderType , String assetSymbol);
    Order processOrder(Cryptocurrency coin, double quantity , OrderType orderType, User user);
}
