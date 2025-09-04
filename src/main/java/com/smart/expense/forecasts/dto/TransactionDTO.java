package com.smart.expense.forecasts.dto;

import lombok.Data;


import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Data
public class TransactionDTO {
    private String transactionId;
    private ZonedDateTime date;
    private String merchant;
    private BigDecimal amount;
    private String currency;
    private String category;

}
