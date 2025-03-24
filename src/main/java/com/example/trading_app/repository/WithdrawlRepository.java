package com.example.trading_app.repository;

import com.example.trading_app.Entity.Withdrawl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawlRepository extends JpaRepository<Withdrawl,Long> {
    List<Withdrawl>findByUserId(Long userId);
}
