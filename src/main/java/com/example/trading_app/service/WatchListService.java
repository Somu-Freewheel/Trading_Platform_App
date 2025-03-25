package com.example.trading_app.service;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.WatchList;

public interface WatchListService {
    WatchList findUserWatchList(Long userId);
    WatchList createWatchList(User user);
    WatchList findById(Long id);
    Cryptocurrency addItemToWatchList(Cryptocurrency coin , User user);
}
