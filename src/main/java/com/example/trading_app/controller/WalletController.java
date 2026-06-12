package com.example.trading_app.controller;
import com.example.trading_app.Entity.*;
import com.example.trading_app.response.PaymentResponse;
import com.example.trading_app.service.OrderService;
import com.example.trading_app.service.PaymentService;
import com.example.trading_app.service.UserService;
import com.example.trading_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
    @PutMapping("/deposit")
    public ResponseEntity<Wallet>addMoneyToWallet(@RequestHeader("Authorization")String jwt,
                                                 @RequestParam(name ="order_id") Long orderId,
                                                 @RequestParam(name ="payment_Id")String paymentId,
                                                 @RequestBody WalletTransaction req) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        PaymentOrder order = paymentService.getPaymentOrderById(orderId);
        Boolean paymentStatus = paymentService.ProceedPaymentOrder(order,paymentId);
        if(paymentStatus){
            wallet = walletService.addBalance(wallet,order.getAmount());
        }
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

    // Test endpoint for adding balance directly (for testing only)
    @PostMapping("/test/add-balance")
    public ResponseEntity<Wallet>addBalanceForTesting(@RequestHeader("Authorization")String jwt,
                                                      @RequestBody WalletTransaction req) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("User: " + user.getId() + ", Amount: " + req.getAmount());
        Wallet wallet = walletService.getUserWallet(user);
        System.out.println("Wallet before: " + wallet.getBalance());
        wallet = walletService.addBalance(wallet, req.getAmount());
        System.out.println("Wallet after: " + wallet.getBalance());
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

}
