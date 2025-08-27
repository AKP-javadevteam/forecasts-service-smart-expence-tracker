package com.smart.expense.forecasts.service;

import com.smart.expense.forecasts.dto.TransactionDTO;
import com.smart.expense.forecasts.model.Insight;
import com.smart.expense.forecasts.repository.InsightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final InsightRepository insightRepository;

    public void generateAndSaveInsights(String userId, List<TransactionDTO> transactions, Map<String, BigDecimal> budgets){
        // Clear out old insights for this user to avoid duplicates
        insightRepository.deleteAll(insightRepository.findByUserId(userId));

        // --- Insight #1: Overspend Warning ---
        Map<String, BigDecimal> spendByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, t -> t.getAmount().abs(), BigDecimal::add)
                ));

        spendByCategory.forEach((category, spend) -> {
            BigDecimal budget = budgets.getOrDefault(category, BigDecimal.ZERO);
            if (budget.compareTo(BigDecimal.ZERO) > 0 && spend.compareTo(budget) > 0){
                String message = String.format("You have spend $%s of your $%s budget for %s", spend, budget, category);
                Insight overspendInsight = new Insight(userId, "OVERSPEND_WARNING", message);
                insightRepository.save(overspendInsight);
                System.out.println("Generated insight: " + message);
            }
        });

        // --- Insight #2: Subscription Candidate (Example)
        // Find merchants with 2 or more small, repeated payments
        Map<String, Long> recurringMerchants = transactions.stream()
                .filter( t -> t.getAmount().abs().compareTo(new BigDecimal("50")) < 0 ) // Filter for small amounts
                .collect(Collectors.groupingBy(TransactionDTO::getMerchant, Collectors.counting()));

        recurringMerchants.forEach((merchant, count) -> {
            if (count >= 2) {
                // Find one transaction to get the amount for the message
                BigDecimal amount = transactions.stream()
                        .filter(t -> t.getMerchant().equals(merchant))
                        .findFirst()
                        .map(t -> t.getAmount().abs())
                        .orElse(BigDecimal.ZERO);

                String message = String.format("We noticed %d payments of $%s to '%s'. Is this a recurring subscription?", count, amount, merchant);
                Insight subsriptionInsight = new Insight(userId, "SUBSCRIPTION_CANDIDATE", message);
                insightRepository.save(subsriptionInsight);
                System.out.println("Generated insight: " + message);

            }
        });
    }


}
