package com.example.trading_app.controller;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Withdrawal;
import com.example.trading_app.service.UserService;
import com.example.trading_app.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService  userService;

    @PostMapping("api/withdrawal/{amount}")
    public ResponseEntity<?>withdrawalRequest(@PathVariable Long amount,
                                            @RequestHeader("Authorization") String jwt)throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,user);

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }
    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?>proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization")String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.proceedWithdrawal(id,accept);
        return new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }
    @GetMapping("/api/withdrawl")
    public ResponseEntity<List<Withdrawal>>getWithdrawalHistory(
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal>withdrawalHistory=withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawalHistory,HttpStatus.OK);

    }
    @GetMapping("/api/admin/withdrawl")
    public ResponseEntity<List<Withdrawal>>getWithdrawalRequest(
            @RequestHeader("Authorization")String jwt)throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal>withdrawalRequest=withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawalRequest,HttpStatus.OK);

    }




}
