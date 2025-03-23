package com.example.trading_app.service;

import com.example.trading_app.Entity.Asset;
import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;
import com.example.trading_app.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService{
    @Autowired
    private AssetRepository assetRepository;
    @Override
    public Asset creatAsset(User user, Cryptocurrency coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId).orElseThrow(()->new RuntimeException("asset not found "));
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(Long userId, Long assetId) {
        return null;
    }


    @Override
    public List<Asset> getUsersAsset(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updatedAsset(Long assetId, double quantity) {
        Asset oldAsset =getAssetById(assetId);
        oldAsset.setQuantity(quantity+ oldAsset.getQuantity());
        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
            assetRepository.deleteById(assetId);
    }
}
