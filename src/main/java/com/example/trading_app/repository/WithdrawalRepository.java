package com.example.trading_app.repository;

import com.example.trading_app.Entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal,Long> {
    List<Withdrawal>findByUserId(Long userId);
}
