package com.smart.expense.forecasts.repository;

import com.smart.expense.forecasts.model.Insight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsightRepository extends MongoRepository<Insight, String> {
    List<Insight> findByUserId(String userId);

}
