package com.smart.expense.forecasts.controller;

import com.smart.expense.forecasts.client.BudgetServiceClient;
import com.smart.expense.forecasts.client.RestTransactionServiceClient;
import com.smart.expense.forecasts.client.TransactionServiceClient;
import com.smart.expense.forecasts.dto.*;
import com.smart.expense.forecasts.model.Insight;
import com.smart.expense.forecasts.repository.InsightRepository;
import com.smart.expense.forecasts.service.InsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final RestTransactionServiceClient transactionServiceClient;
    private final BudgetServiceClient budgetServiceClient;
    private final InsightService insightService;
    private final InsightRepository insightRepository;

    public ReportsController(RestTransactionServiceClient transactionServiceClient, InsightService insightService, BudgetServiceClient budgetServiceClient, InsightRepository insightRepository){
        this.transactionServiceClient = transactionServiceClient;
        this.insightService = insightService;
        this.budgetServiceClient = budgetServiceClient;
        this.insightRepository = insightRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDTO> getReportSummary(@RequestParam String month, Authentication authentication){
        // todo: in the reall app, get the userId from the JWT gtoken in the authorization header
        //String userId = "alice"; // Hardocded for now
        String userId ="test-user-no-auth";

        if (authentication!= null){
            userId = authentication.getName();
        }

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

        return ResponseEntity.ok(report);
    }

    // Helper method for mocked data
    private Map<String, BigDecimal> getMockBudgets(){
        return Map.of(
                "Groceries", new BigDecimal("500.00"),
                "Transport", new BigDecimal("100.00")
        );
    }
    @GetMapping("/insights")
    public ResponseEntity<Map<String, List<Insight>>> getInsights(Authentication authentication){
        // TODO: get userId from JWT
        //String userId = "alice";
        String userId = authentication.getName();

        List<Insight> insights = insightRepository.findByUserId(userId);

        // Wrap the list in an object to match the API spec: insights: [...]
        return ResponseEntity.ok(Map.of("insights", insights));
    }

}
