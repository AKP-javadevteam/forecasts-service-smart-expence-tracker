package com.smart.expense.forecasts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BudgetDTO {
    private String category;
    private BigDecimal amount;
}
