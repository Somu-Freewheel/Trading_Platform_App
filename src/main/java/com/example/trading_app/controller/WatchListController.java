package com.example.trading_app.controller;
import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.WatchList;
import com.example.trading_app.service.CryptoCurrencyService;
import com.example.trading_app.service.UserService;
import com.example.trading_app.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchList")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @GetMapping("/user")
    public ResponseEntity<WatchList>getUserWatchList(
            @RequestHeader("Authorization") String jwt) throws Exception{
        User user =userService.findUserProfileByJwt(jwt);
        WatchList watchList =watchListService.findUserWatchList(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(watchList);
    }
    @PostMapping("/create")
    public ResponseEntity<WatchList>createWatchList(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        WatchList createWatchList=watchListService.createWatchList(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createWatchList);
    }
    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList>getWatchListById(
            @PathVariable Long watchListId) throws  Exception{
        WatchList watchList=watchListService.findById(watchListId);
        return ResponseEntity.ok(watchList);
    }
    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Cryptocurrency>addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception{
      User user = userService.findUserProfileByJwt(jwt);
      Cryptocurrency coin = cryptoCurrencyService.findById(coinId);
        Cryptocurrency addedCoin =watchListService.addItemToWatchList(coin,user);
      return ResponseEntity.ok(addedCoin);
    }




}
