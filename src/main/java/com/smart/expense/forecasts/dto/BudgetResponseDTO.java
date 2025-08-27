package com.smart.expense.forecasts.dto;

import lombok.Data;

import java.util.List;

@Data
public class BudgetResponseDTO {
    private List<BudgetDTO> budgets;
}
