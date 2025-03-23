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
        return asset;
    }

    @Override
    public Asset getAssetById(Long assetId) {
        List<Asset>assets =assetRepository.findByUserId(assetId);
        return assets.get(0);
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public Asset getUsersAsset(Long userId) {
        return null;
    }

    @Override
    public Asset updatedAsset(Long assetId, double quantity) {
        return null;
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return null;
    }

    @Override
    public void deleteAsset(Long AssetId) {

    }
}
