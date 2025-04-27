package com.example.trading_app.repository;

import com.example.trading_app.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletServiceRepository extends JpaRepository<Wallet,Long> {
    Wallet findByUserId(Long userId);
}
