package com.example.trading_app.service;

import com.example.trading_app.Entity.Asset;
import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;

public interface AssetService {
    Asset creatAsset(User user , Cryptocurrency coin , double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    Asset getUsersAsset(Long userId);
    Asset updatedAsset(Long assetId,double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long AssetId);

}
