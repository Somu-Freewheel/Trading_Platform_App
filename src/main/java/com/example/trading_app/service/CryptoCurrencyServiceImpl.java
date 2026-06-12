package com.example.trading_app.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
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
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService{
	@Autowired
	private CryptoCurrencyRepository cryptoCurrencyRepository;
	@Autowired
	private ObjectMapper objectMapper;
	public CryptoCurrencyServiceImpl() {
		System.out.println("CryptoCurrencyServiceImpl Bean Created ✅");
	}

	@Override
	public List<Cryptocurrency> getCoinList(int page) {
		// TODO Auto-generated method stub
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/coins/markets")
				.queryParam("vs_currency", "usd")
				.queryParam("per_page", "10")
				.queryParam("page", page)
				.toUriString();
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
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/coins/{coinId}/market_chart")
				.queryParam("vs_currency", "usd")
				.queryParam("per_page", "10")
				.queryParam("days", days)
				.buildAndExpand(coinId)
				.toUriString();
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
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/coins/{coinId}")
				.buildAndExpand(coinId)
				.toUriString();
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers= new HttpHeaders();
			HttpEntity<String>entity= new HttpEntity<String>("parameters",headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			Cryptocurrency coin = new Cryptocurrency();

			// Safe field extraction with null checks
			if (jsonNode.has("id") && !jsonNode.get("id").isNull()) {
				coin.setId(jsonNode.get("id").asText());
			}
			if (jsonNode.has("name") && !jsonNode.get("name").isNull()) {
				coin.setName(jsonNode.get("name").asText());
			}
			if (jsonNode.has("symbol") && !jsonNode.get("symbol").isNull()) {
				coin.setSymbol(jsonNode.get("symbol").asText());
			}
			if (jsonNode.has("image") && jsonNode.get("image").has("large") && !jsonNode.get("image").get("large").isNull()) {
				coin.setImage(jsonNode.get("image").get("large").asText());
			}

			JsonNode marketData = jsonNode.get("market_data");
			if (marketData != null && !marketData.isNull()) {
				if (marketData.has("current_price") && marketData.get("current_price").has("usd") && !marketData.get("current_price").get("usd").isNull()) {
					coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
				}
				if (marketData.has("market_cap") && marketData.get("market_cap").has("usd") && !marketData.get("market_cap").get("usd").isNull()) {
					coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
				}
				if (marketData.has("market_cap_rank") && !marketData.get("market_cap_rank").isNull()) {
					coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
				}
				if (marketData.has("total_volume") && marketData.get("total_volume").has("usd") && !marketData.get("total_volume").get("usd").isNull()) {
					coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
				}
				if (marketData.has("high_24h") && marketData.get("high_24h").has("usd") && !marketData.get("high_24h").get("usd").isNull()) {
					coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
				}
				if (marketData.has("low_24h") && marketData.get("low_24h").has("usd") && !marketData.get("low_24h").get("usd").isNull()) {
					coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
				}
				if (marketData.has("price_change_24h") && marketData.get("price_change_24h").has("usd") && !marketData.get("price_change_24h").get("usd").isNull()) {
					coin.setPriceChange24h(marketData.get("price_change_24h").get("usd").asDouble());
				}
				if (marketData.has("market_cap_change_24h") && !marketData.get("market_cap_change_24h").isNull()) {
					coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
				}
				if (marketData.has("market_cap_change_percentage_24h") && !marketData.get("market_cap_change_percentage_24h").isNull()) {
					coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
				}
				if (marketData.has("total_supply") && !marketData.get("total_supply").isNull()) {
					coin.setTotalSupply(marketData.get("total_supply").asLong());
				}
			}

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
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/search")
				.queryParam("query", keyword)
				.toUriString();
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
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/coins/markets")
				.queryParam("vs_currency", "usd")
				.queryParam("per_page", "50")
				.queryParam("page", "1")
				.toUriString();
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
		String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/search/trending")
				.toUriString();
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
