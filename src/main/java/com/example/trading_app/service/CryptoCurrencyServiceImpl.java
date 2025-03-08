package com.example.trading_app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.trading_app.Entity.Cryptocurrency;
import com.example.trading_app.repository.CryptoCurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//Here we are implementing the business logic for CryptoCurrency 
//We are using Coin Gecko API . 
//We are integrating the Coin Gecko API 

public class CryptoCurrencyServiceImpl implements CryptoCurrencyService{
	@Autowired
	private CryptoCurrencyRepository cryptoCurrencyRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<Cryptocurrency> getCoinList(int page) {
		// TODO Auto-generated method stub
		String url ="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			List<Cryptocurrency>coinList=objectMapper.readValue(
					response.getBody(),
					new TypeReference<List<Cryptocurrency>>() {});
			return coinList;
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}
		
	}

	@Override
	public String getMarketChart(String coinId, int days) {
		// TODO Auto-generated method stub
		String url ="https://api.coingecko.com/api/v3/coins/"+coinId+"/market_chart?vs_currency=usd&per_page=10&days="+days;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}		
	}

	@Override
	public String getCoinDetails(String coinId) {
		// TODO Auto-generated method stub
		//to check the actual url 
		//https://api.coingecko.com/api/v3/coins/bitcoin
		String url ="https://api.coingecko.com/api/v3/coins/"+coinId;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			Cryptocurrency coin = new Cryptocurrency();
			coin.setId(jsonNode.get("id").asText());
			coin.setName(jsonNode.get("name").asText());
			coin.setSymbol(jsonNode.get("symbol").asText());
			coin.setImage(jsonNode.get("image").get("large").asText());
			JsonNode marketData = jsonNode.get("market_data");
			coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
			coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
			coin.setMarketCapRank(marketData.get("market_cap_rank").get("usd").asInt());
			coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
			coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
			coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
			coin.setPriceChange24h(marketData.get("price_change_24h").get("usd").asDouble());
			coin.setPriceChange24h(marketData.get("price_change_24h").get("usd").asDouble());
			coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
			coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
			coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());
			cryptoCurrencyRepository.save(coin);
			return response.getBody();
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}				
	}

	@Override
	public Cryptocurrency findById(String coinId) {
		// TODO Auto-generated method stub
		Optional<Cryptocurrency>optionalCoin=cryptoCurrencyRepository.findById(coinId);
		if(optionalCoin.isEmpty()) {
			throw new RuntimeException("coin not found");
		}
		return optionalCoin.get();
	}

	@Override
	public String searchCoin(String keyword) {
		// TODO Auto-generated method stub
		String url ="https://api.coingecko.com/api/v3/coins/="+keyword;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}	
	}

	@Override
	public String getTopFiftyCoinByMarketCapRank() {
		// TODO Auto-generated method stub
		String url ="https://api.coingecko.com/api/v3/coins/markets/vs_currency=usd&per_page=50&page=1";
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}	
		
	}

	@Override
	public String getTradingCoins() {
		// TODO Auto-generated method stub
		String url ="https://api.coingecko.com/api/v3/search/trading";
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException("Error fetching coin list: " + e.getMessage(), e);
		}catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
		}	
		
	}

}
