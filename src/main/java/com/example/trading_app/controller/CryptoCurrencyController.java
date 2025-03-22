package com.example.trading_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.service.CryptoCurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/coins")
public class CryptoCurrencyController {
	@Autowired
	private CryptoCurrencyService cryptoCurrencyService;
	@Autowired 
	private ObjectMapper objectMapper;
	@GetMapping
	ResponseEntity<List<Cryptocurrency>>getCoinList(@RequestParam("page")int page)throws Exception{
		List<Cryptocurrency> coins = cryptoCurrencyService.getCoinList(page);
		return new ResponseEntity<>(coins,HttpStatus.ACCEPTED);
		
	}
	@GetMapping("/{coinId}/chart")
	ResponseEntity<JsonNode>getMarketChart(@PathVariable String coinId,
			@RequestParam("days")int days)throws Exception{
		String response= cryptoCurrencyService.getMarketChart(coinId,days);
		JsonNode jsonNode =objectMapper.readTree(response);
		
		return new ResponseEntity<>(jsonNode,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	ResponseEntity<JsonNode>searchCoin(@RequestParam("q")String keyword)throws Exception{
		String coin =cryptoCurrencyService.searchCoin(keyword);
		JsonNode jsonNode =objectMapper.readTree(coin);
		
		return ResponseEntity.ok(jsonNode);
	}
	
	@GetMapping("/top50")
	ResponseEntity<JsonNode>getTop50MarketCapRank() throws JsonProcessingException{
		String coin = cryptoCurrencyService.getTopFiftyCoinByMarketCapRank();
		JsonNode jsonNode =objectMapper.readTree(coin);
		return  ResponseEntity.ok(jsonNode);
		
	}
	@GetMapping("/trading")
	ResponseEntity<JsonNode>getTradingCoins() throws JsonProcessingException{
		String coin = cryptoCurrencyService.getTradingCoins();
		JsonNode jsonNode =objectMapper.readTree(coin);
		return  ResponseEntity.ok(jsonNode);
	}
	@GetMapping("/details/{coinId}")
	ResponseEntity<JsonNode>getCoinDetails(@PathVariable String coinId)throws JsonProcessingException{
		String coinDetails = cryptoCurrencyService.getCoinDetails(coinId);
		JsonNode jsonNode =objectMapper.readTree(coinDetails);
		return  ResponseEntity.ok(jsonNode);
	}
	
	
	
	

}
