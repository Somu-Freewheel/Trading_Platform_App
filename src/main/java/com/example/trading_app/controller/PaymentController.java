package com.example.trading_app.controller;
import com.example.trading_app.Entity.PaymentOrder;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.PaymentMethod;
import com.example.trading_app.response.PaymentResponse;
import com.example.trading_app.service.PaymentService;
import com.example.trading_app.service.UserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse>paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization")String jwt)
            throws Exception, RazorpayException, StripeException {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse;
        PaymentOrder paymentOrder =paymentService.createOrder(user,amount,paymentMethod);
        if (paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse=paymentService.createRazorPayPaymentUrl(user,amount);
        }else{
            paymentResponse=paymentService.createStripePaymentUrl(user,amount, paymentOrder.getId().toString());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

}
