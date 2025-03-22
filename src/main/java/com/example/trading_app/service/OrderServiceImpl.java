package com.example.trading_app.service;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.OrderItem;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.OrderStatus;
import com.example.trading_app.domain.OrderType;
import com.example.trading_app.repository.OrderItemRepository;
import com.example.trading_app.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WalletService walletService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimeStamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderId(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(()->new Exception("order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.finfByUserId(userId);
    }

    private OrderItem createOrderItem(Cryptocurrency coin , double quantity , double buyPrice , double sellPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }
    //Buying the asset
    @Transactional
    public Order buyAsset(Cryptocurrency coin , double quantity, User user){
        if(quantity<=0){
            throw new RuntimeException("quantity should be greater than zero");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin ,quantity, buyPrice , 0);
        Order order = createOrder(user,orderItem,OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order,user);
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);
//create Asset
        return savedOrder;

    }
    //Selling the asset
    @Transactional
    public Order sellAsset(Cryptocurrency coin , double quantity, User user){
        if(quantity<=0){
            throw new RuntimeException("quantity should be greater than zero");
        }
        double sellPrice = coin.getCurrentPrice();
        double buyPrice = assetToSell.getPrice();
        OrderItem orderItem = createOrderItem(coin ,quantity,buyPrice ,sellPrice);
        Order order = createOrder(user,orderItem,OrderType.SELL);
        orderItem.setOrder(order);
        if(assetToSell.getQuantity()>=quantity){
            order.setOrderStatus(OrderStatus.SUCCESS);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepository.save(order);
            walletService.payOrderPayment(order,user);
            Asset updatedAsset=assetService.updatedAsset(assetToSell.getId(),quantity);
            if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                assetService.deleteAsset(updatedAsset.getId);
            }
            return savedOrder;
        }
        throw new RuntimeException("Insufficient Quantity to sell");
    }

    @Override
    public Order processOrder(Cryptocurrency coin, double quantity, OrderType orderType, User user) {
        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin ,quantity,user);
        }else if(orderType.equals(OrderType.SELL)){
            return sellAsset(coin,quantity,user);
        }
        throw new RuntimeException("Invalid order type");
    }
}
