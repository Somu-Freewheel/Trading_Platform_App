package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Withdrawal;
import com.example.trading_app.domain.WithdrawalStatus;
import com.example.trading_app.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawlId, boolean accept) {
        Optional<Withdrawal>userWithdrawlId= withdrawalRepository.findById(withdrawlId);
        if(userWithdrawlId.isEmpty()){
            throw new RuntimeException("user withdrawl id not found");
        }
        Withdrawal withdrawal = userWithdrawlId.get();
        withdrawal.setDate(LocalDateTime.now());
        if(accept){
            withdrawal.setStatus(WithdrawalStatus.SUCCESS);
        }else{
            withdrawal.setStatus(WithdrawalStatus.PENDING);
        }

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
