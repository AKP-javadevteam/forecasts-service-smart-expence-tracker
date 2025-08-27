package com.smart.expense.forecasts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "insights")
public class Insight {

    @Id
    private String id; // mongoDB uses String IDs by default
    private String userId;
    private String type; // example: overspend warning
    private String message;

    public Insight(String userId, String type, String message) {
        this.userId = userId;
        this.type = type;
        this.message = message;
    }

}
