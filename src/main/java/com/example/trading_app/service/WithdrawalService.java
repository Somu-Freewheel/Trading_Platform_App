package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Withdrawal;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithdrawal(Long amount , User user);
    Withdrawal proceedWithdrawal(Long withdrawlId, boolean accept);
    List<Withdrawal>getUsersWithdrawalHistory(User user);
    List<Withdrawal>getAllWithdrawalRequest();
}
