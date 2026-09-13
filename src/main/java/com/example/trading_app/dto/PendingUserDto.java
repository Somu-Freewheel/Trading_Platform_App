package com.example.trading_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserDto implements Serializable {
    private String fullName;
    private String email;
    private String encodedPassword;
    private String otp;

}
