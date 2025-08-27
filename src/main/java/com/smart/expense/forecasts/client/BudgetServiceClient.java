package com.smart.expense.forecasts.client;

import com.smart.expense.forecasts.dto.BudgetDTO;
import com.smart.expense.forecasts.dto.BudgetResponseDTO;

import java.util.List;

public interface BudgetServiceClient {
    BudgetResponseDTO getBudgetsForUser(String userId, String month);
}
