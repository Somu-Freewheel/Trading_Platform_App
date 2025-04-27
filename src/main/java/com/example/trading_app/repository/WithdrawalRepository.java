package com.example.trading_app.repository;

import com.example.trading_app.Entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal,Long> {
    List<Withdrawal>findByUserId(Long userId);
}
