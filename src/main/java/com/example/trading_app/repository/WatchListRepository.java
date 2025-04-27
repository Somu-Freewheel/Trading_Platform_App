package com.example.trading_app.repository;

import com.example.trading_app.Entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList,Long> {
    WatchList findByUserId(Long userId);
}
