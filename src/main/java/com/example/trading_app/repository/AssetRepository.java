package com.example.trading_app.repository;

import com.example.trading_app.Entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AssetRepository extends JpaRepository<Asset,Long> {
    List<Asset>findByUserId(Long userId);
    Asset findByUserIdAndCoinId(Long userId ,String coinId);
}
