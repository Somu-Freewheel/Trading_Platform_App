package com.example.trading_app.service;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.WatchList;
import com.example.trading_app.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class WatchListServiceImpl implements WatchListService{
    @Autowired
    private WatchListRepository watchListRepository;

    @Override
    public WatchList findUserWatchList(Long userId) {
        WatchList watchList = watchListRepository.findByUserId(userId);
        if(watchList==null){
            throw  new RuntimeException("User not found");
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList =  new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) {
        Optional<WatchList>watchListOptional=watchListRepository.findById(id);
        if(watchListOptional.isEmpty()){
            throw new RuntimeException("WatchList not found");
        }
        return watchListOptional.get();
    }

    @Override
    public Cryptocurrency addItemToWatchList(Cryptocurrency coin, User user) {
        WatchList watchListItem = findUserWatchList(user.getId());
        if(watchListItem.getCoins().contains(coin)){
            watchListItem.getCoins().remove(coin);
        }else{
            watchListItem.getCoins().add(coin);
            watchListRepository.save(watchListItem);
        }
        return coin;
    }
}
