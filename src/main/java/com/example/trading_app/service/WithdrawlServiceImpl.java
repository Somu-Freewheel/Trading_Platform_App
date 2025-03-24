package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Withdrawl;
import com.example.trading_app.domain.WithdrawlStatus;
import com.example.trading_app.repository.WithdrawlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawlServiceImpl implements WithdrawlService{
    @Autowired
    private WithdrawlRepository withdrawlRepository;

    @Override
    public Withdrawl requestWithdrawl(Long amount, User user) {
        Withdrawl withdrawl= new Withdrawl();
        withdrawl.setAmount(amount);
        withdrawl.setUser(user);
        withdrawl.setStatus(WithdrawlStatus.PENDING);
        return withdrawlRepository.save(withdrawl);
    }

    @Override
    public Withdrawl proceedWithdrawl(Long withdrawlId, boolean accept) {
        Optional<Withdrawl>userWithdrawlId=withdrawlRepository.findById(withdrawlId);
        if(userWithdrawlId.isEmpty()){
            throw new RuntimeException("user withdrawl id not found");
        }
        Withdrawl withdrawl = userWithdrawlId.get();
        withdrawl.setDate(LocalDateTime.now());
        if(accept){
            withdrawl.setStatus(WithdrawlStatus.SUCCESS);
        }else{
            withdrawl.setStatus(WithdrawlStatus.PENDING);
        }

        return withdrawlRepository.save(withdrawl);
    }

    @Override
    public List<Withdrawl> getUsersWithdrawlHistory(User user) {
        return withdrawlRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawl> getAllWithdrawlRequest() {
        return withdrawlRepository.findAll();
    }
}
