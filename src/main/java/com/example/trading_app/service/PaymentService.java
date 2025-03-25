package com.example.trading_app.service;

import com.example.trading_app.Entity.PaymentOrder;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.PaymentMethod;
import com.example.trading_app.response.PaymentResponse;
import com.razorpay.RazorpayException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount , PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id);
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;
    PaymentResponse createRazorPayPaymentUrl(User user , Long amount);
    PaymentResponse createStripePaymentUrl(User user,Long amount , String orderId);
}
