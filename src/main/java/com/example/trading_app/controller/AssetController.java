package com.example.trading_app.controller;

import com.example.trading_app.Entity.Asset;
import com.example.trading_app.Entity.User;
import com.example.trading_app.service.AssetService;
import com.example.trading_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }
    @GetMapping("/{asstId}")
    public ResponseEntity<Asset>getAssetById(@PathVariable Long assetId){
        Asset asset = assetService.getAssetById(assetId);
        return  ResponseEntity.ok().body(asset);

    }
    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset>getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization")String jwt){
        User user =userService.findUserProfileByJwt(jwt);
        Asset asset=assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }
    @GetMapping()
    public ResponseEntity<List<Asset>>getAssetForUser(
            @RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset>assets=assetService.getUsersAsset(user.getId());
        return ResponseEntity.ok().body(assets);
    }

}
