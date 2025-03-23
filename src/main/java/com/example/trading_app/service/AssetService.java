package com.example.trading_app.service;

import com.example.trading_app.Entity.Asset;
import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;

import java.util.List;

public interface AssetService {
    Asset creatAsset(User user , Cryptocurrency coin , double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserIdAndAssetId(Long userId, Long assetId);
    List<Asset>getUsersAsset(Long userId);
    Asset updatedAsset(Long assetId,double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long AssetId);

}
