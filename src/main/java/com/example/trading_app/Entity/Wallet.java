package com.example.trading_app.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "wallet_id_seq")
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private BigDecimal balance;



}
