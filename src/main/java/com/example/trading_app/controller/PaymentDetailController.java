package com.example.trading_app.controller;

import com.example.trading_app.Entity.PaymentDetails;
import com.example.trading_app.Entity.User;
import com.example.trading_app.service.PaymentDetailsService;
import com.example.trading_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {
    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails>addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsRequest,
            @RequestHeader("Authorization") String jwt) throws  Exception{
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getIfscCode(),
                paymentDetailsRequest.getBankName(),
                user);
        return new ResponseEntity<>(paymentDetails,HttpStatus.CREATED);
    }
    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails>getUsersPaymentDetails(
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails=paymentDetailsService.getUserPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails,HttpStatus.CREATED);
    }


}
