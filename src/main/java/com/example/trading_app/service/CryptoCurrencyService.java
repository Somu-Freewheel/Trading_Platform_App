package com.example.trading_app.service;

import java.util.List;

import com.example.trading_app.Entity.Cryptocurrency;

public interface CryptoCurrencyService {
	List<Cryptocurrency> getCoinList(int page);
	String getMarketChart(String coinId,int days);
	String getCoinDetails(String coinId);
	Cryptocurrency findById(String coinId);
	String searchCoin(String coinId);
	String getTopFiftyCoinByMarketCapRank();
	String getTradingCoins();
	

}
