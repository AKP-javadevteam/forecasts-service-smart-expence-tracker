package com.smart.expense.forecasts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReportSummaryDTO {
    private String requestedMonth;
    private BigDecimal totalSpend;
    private String spendStatus;
    // adding the category breakdown list
    private List<CategoryBreakdownDTO> categoryBreakdown;
}
