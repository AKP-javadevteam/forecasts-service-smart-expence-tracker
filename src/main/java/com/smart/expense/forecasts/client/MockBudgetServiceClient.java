package com.smart.expense.forecasts.client;

import com.smart.expense.forecasts.dto.BudgetDTO;
import com.smart.expense.forecasts.dto.BudgetResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MockBudgetServiceClient implements BudgetServiceClient{

    @Override
    public BudgetResponseDTO getBudgetsForUser(String userId, String month){
        System.out.println("--- Using MockBudgetServiceClient ---");

        List<BudgetDTO> mockBudgets = List.of(
                new BudgetDTO("Groceries", new BigDecimal("500.00")),
                new BudgetDTO("Transport", new BigDecimal("100.00")),
                new BudgetDTO("Entertainment", new BigDecimal("75.00"))
        );

        BudgetResponseDTO response = new BudgetResponseDTO();
        response.setBudgets(mockBudgets);
        return  response;
    }
}
