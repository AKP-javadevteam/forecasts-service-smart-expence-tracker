package com.smart.expense.forecasts.service;

import com.smart.expense.forecasts.client.BudgetServiceClient;
import com.smart.expense.forecasts.client.RestTransactionServiceClient;
import com.smart.expense.forecasts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final RestTransactionServiceClient transactionServiceClient;
    private final BudgetServiceClient budgetServiceClient;
    private final InsightService insightService;

    public ReportSummaryDTO generateReportSummary(String userId, String month){

        // 1. Call the transaction service
        TransactionResponseDTO transactionData = transactionServiceClient.getTransactionsForUser(userId, month);

        // 2. Calculate spend per caegory using Java Streams
        Map<String, BigDecimal> spendByCategory = transactionData.getTransactions().stream()
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, t -> t.getAmount().abs(), BigDecimal::add )
                ));

        // 3. Get mock budget data (this would be another microservice call to the budget microservice)
        // This call uses the in-memory MockBudgetServiceClient
        BudgetResponseDTO budgetData = budgetServiceClient.getBudgetsForUser(userId, month);

        // --- FIX: Convert the list of budgets into a usable Map ---
        Map<String, BigDecimal> budgetMap = budgetData.getBudgets().stream()
                .collect(Collectors.toMap(BudgetDTO::getCategory, BudgetDTO::getAmount));

        // 4. Create the category breakdodwn DTO list
        List<CategoryBreakdownDTO> categoryBreakdown = spendByCategory.entrySet().stream()
                .map(entry -> new CategoryBreakdownDTO(
                        entry.getKey(),
                        entry.getValue(),
                        budgetMap.getOrDefault(entry.getKey(), BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());

        // 5. Calculate total spend
        BigDecimal totalSpend = categoryBreakdown.stream()
                .map(CategoryBreakdownDTO::getSpend)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // 6. Build the final response
        ReportSummaryDTO report =  ReportSummaryDTO.builder()
                .requestedMonth(month)
                .totalSpend(totalSpend)
                .spendStatus("On Track") // Mock status
                .categoryBreakdown(categoryBreakdown)
                .build();

        // 7. Generate and save insights based on fetched data
        insightService.generateAndSaveInsights(userId, transactionData.getTransactions(), budgetMap );

        return ReportSummaryDTO.builder()
                .requestedMonth(month)
                .totalSpend(totalSpend)
                .spendStatus("On Track") // mock status
                .categoryBreakdown(categoryBreakdown)
                .build();

    }
}
