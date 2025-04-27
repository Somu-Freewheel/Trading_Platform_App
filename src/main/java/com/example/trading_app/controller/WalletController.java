package com.example.trading_app.controller;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Wallet;
import com.example.trading_app.Entity.WalletTransaction;
import com.example.trading_app.service.OrderService;
import com.example.trading_app.service.PaymentService;
import com.example.trading_app.service.UserService;
import com.example.trading_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @GetMapping("")
    public ResponseEntity<Wallet>getUserWallet(@RequestHeader("Authorization")String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
    // PUT : /api/wallet/{walletId}/transfer
    @PutMapping("{walletId}/transfer")
    public ResponseEntity<Wallet>walletToWalletTransfer(@RequestHeader("Authorization")String jwt,
                                                        @PathVariable Long walletId,
                                                        @RequestBody WalletTransaction req) throws Exception{
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet =walletService.walletToWalletTransfer(senderUser,receiverWallet,
                req.getAmount());
        return new ResponseEntity<>( wallet, HttpStatus.OK);

    }
    //PUT : /api/wallet/order/{orderId}/pay
    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet>payOrderPayment(@RequestHeader("Authorization")String jwt,
                                                        @PathVariable Long orderId,
                                                        @RequestBody WalletTransaction req) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderId(orderId);
        Wallet wallet =walletService.payOrderPayment(order,user);
        return new ResponseEntity<>( wallet, HttpStatus.ACCEPTED);
    }

}
