package com.example.trading_app.controller;
import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.OrderType;
import com.example.trading_app.request.CreateOrderRequest;
import com.example.trading_app.service.CryptoCurrencyService;
import com.example.trading_app.service.OrderService;
import com.example.trading_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoCurrencyService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order>payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req) {
        User user = userService.findUserProfileByJwt(jwt);
        Cryptocurrency coin = coinService.findById(req.getCoinId());
        Order order = orderService.processOrder(coin,req.getQuantity(),req.getOrderType(),user);
        return ResponseEntity.ok(order);

    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order>getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId) throws Exception {
        User user =userService.findUserProfileByJwt(jwtToken);
        Order order = orderService.getOrderId(orderId);
        if(order.getUser().getId().equals(user.getId())){
            return  ResponseEntity.ok(order);
        }else{
            throw new Exception("Invalid User");
        }
    }
    @GetMapping()
    public ResponseEntity<List<Order>>getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false)String asset_symbol) throws Exception {
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order>userOrders= orderService.getAllOrdersOfUser(userId ,order_type,asset_symbol);
        return ResponseEntity.ok(userOrders);
    }



}
