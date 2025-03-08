package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.Cryptocurrency;

public interface CryptoCurrencyRepository extends JpaRepository<Cryptocurrency,String>{

}
