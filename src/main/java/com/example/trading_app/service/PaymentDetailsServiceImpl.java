package com.example.trading_app.service;

import com.example.trading_app.Entity.PaymentDetails;
import com.example.trading_app.Entity.User;
import com.example.trading_app.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) {
        return null;
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return null;
    }
}
