package com.smart.expense.forecasts.controller;

import com.smart.expense.forecasts.client.BudgetServiceClient;
import com.smart.expense.forecasts.client.RestTransactionServiceClient;
import com.smart.expense.forecasts.client.TransactionServiceClient;
import com.smart.expense.forecasts.dto.*;
import com.smart.expense.forecasts.model.Insight;
import com.smart.expense.forecasts.repository.InsightRepository;
import com.smart.expense.forecasts.service.InsightService;
import com.smart.expense.forecasts.service.ReportsService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;
    private final InsightRepository insightRepository;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDTO> getReportSummary(@RequestParam String month, Authentication authentication){
        // todo: in the reall app, get the userId from the JWT gtoken in the authorization header
        //String userId = "alice"; // Hardocded for now
        String userId ="test-user-no-auth";

        if (authentication!= null){
            userId = authentication.getName();
        }
        // Delega everything to service class ReportsService
        ReportSummaryDTO report = reportsService.generateReportSummary(userId, month);



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
        // String userId = authentication.getName();

        String userId = "test-user-no-auth";

        List<Insight> insights = insightRepository.findByUserId(userId);

        // Wrap the list in an object to match the API spec: insights: [...]
        return ResponseEntity.ok(Map.of("insights", insights));
    }

}
