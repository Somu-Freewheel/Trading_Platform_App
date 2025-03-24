package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Withdrawl;

import java.util.List;

public interface WithdrawlService {
    Withdrawl requestWithdrawl(Long amount , User user);
    Withdrawl proceedWithdrawl(Long withdrawlId,boolean accept);
    List<Withdrawl>getUsersWithdrawlHistory(User user);
    List<Withdrawl>getAllWithdrawlRequest();
}
