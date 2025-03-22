package com.example.trading_app.service;

import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet , Long money);
    Wallet findWalletById(Long id);
    Wallet walletToWalletTransfer(User sender , Wallet receiverWallet,Long amount);
    Wallet payOrderPayment(Order order, User user);
}
