package com.smart.expense.forecasts.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionResponseDTO {
    private List<TransactionDTO> transactions;
}
