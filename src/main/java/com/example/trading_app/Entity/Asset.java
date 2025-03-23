package com.example.trading_app.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  double quantity;
    private double buyPrice;

    @ManyToOne
    private User user;

    @ManyToOne
    private Cryptocurrency coin;

}
