Forecasts, Insights & Reports Service
Overview
The Forecasts, Insights & Reports Service is the primary analytical engine of the Smart Expense Tracker application. Its purpose is to consume raw financial data from other services, perform calculations and analysis, and generate high-level summaries, reports, and actionable insights for the end-user.

This service is built with Spring Boot and uses a MongoDB database to store generated analytical documents like insights.

Core Responsibilities
Data Aggregation: Fetches raw transaction and budget data from upstream services.

Report Generation: Creates aggregated financial summaries, such as total monthly spend and category-based breakdowns.

Insight Analysis: Applies business logic to the raw data to identify financial patterns, such as potential overspending or recurring payments that may be untracked subscriptions.

Data Storage: Persists the generated insights as documents in a MongoDB collection for later retrieval.

API Endpoints
Public API
These endpoints are intended to be called by the API Gateway after a user has been authenticated.

GET /reports/summary

Description: The main endpoint for generating a user's monthly financial report. It orchestrates calls to the Transaction Service and Users & Budgets Service, combines the data, and returns a comprehensive summary. This call also triggers the generation and saving of new insights.

Authentication: Requires a valid JWT.

GET /reports/insights

Description: Retrieves all previously generated insights for the authenticated user from the MongoDB database.

Authentication: Requires a valid JWT.

Communication
Inbound: Receives requests from the API Gateway for all its public endpoints.

Outbound:

Transaction Service: Calls GET /internal/transactions-by-user/{userId} to fetch raw transaction data.

Users & Budgets Service: Calls GET /internal/budgets-by-user/{userId} to fetch the user's budget data for a given month.

Database: Connects to the MongoDB database to read and write insight documents.