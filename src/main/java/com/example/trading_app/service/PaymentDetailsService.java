package com.example.trading_app.service;

import com.example.trading_app.Entity.PaymentDetails;
import com.example.trading_app.Entity.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user);
    public PaymentDetails getUserPaymentDetails(User user);
}
