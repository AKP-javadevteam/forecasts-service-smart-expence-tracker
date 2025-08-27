package com.smart.expense.forecasts.client;

import com.smart.expense.forecasts.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTransactionServiceClient {

    private final RestTemplate restTemplate;
    private final String transactionServiceUrl;

    public RestTransactionServiceClient(RestTemplate restTemplate, @Value("${transactionServiceUrl}") String transactionServiceUrl){
        this.restTemplate  = restTemplate;
        this.transactionServiceUrl = transactionServiceUrl;
    }
    public TransactionResponseDTO getTransactionsForUser(String userId, String month ){
        System.out.println(" --- Using REAL RestTransactionServiceClient ---");
        String url = String.format("%s/internal/transactions-by-user/%s?month=%s", transactionServiceUrl, userId, month);
        return restTemplate.getForObject(url, TransactionResponseDTO.class);
    }

}
