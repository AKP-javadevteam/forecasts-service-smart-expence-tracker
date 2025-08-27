package com.smart.expense.forecasts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryBreakdownDTO {
    private String category;
    private BigDecimal spend;
    private BigDecimal budget; // We'll add this from a mocked source for now
}
