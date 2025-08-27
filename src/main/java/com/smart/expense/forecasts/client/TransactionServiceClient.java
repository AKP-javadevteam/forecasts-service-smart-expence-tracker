package com.smart.expense.forecasts.client;

import com.smart.expense.forecasts.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public interface TransactionServiceClient {

    TransactionResponseDTO getTransactionsForUser(String userId, String month);

}
