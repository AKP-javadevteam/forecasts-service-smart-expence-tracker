# Forecasts, Insights & Reports Service — Smart Expense Project

This is the **Forecasts Service** for the Smart Expense project.  
It provides **financial forecasting, insights, and reporting APIs**, powered by time-series models and analytics.  
Data is stored in **MongoDB** to support flexible reporting and fast queries.

---

## 🚀 Tech Stack

- **Project**: Maven, Java 17
- **Spring Boot**: 3.5.4

### Dependencies
- **Spring Web** — REST endpoints for forecasts, insights, and reports.
- **Spring Security** — JWT-based resource server for authorization.
- **Spring Data MongoDB** — persistence layer for reports and forecasting results.
- **Validation** — ensure correct request parameters (dates, categories, filters).
- **Spring Boot Actuator** — monitoring and health checks.

---

## 📂 Project Purpose
- Provide **forecasting APIs** (e.g., cash flow predictions, category-specific trends).
- Deliver **insights** such as spending anomalies or budget risks.
- Generate **reports** for dashboards and exports.
- Store and query analytics data in **MongoDB**.
- Secure endpoints with **JWT authentication**.
- Expose monitoring and health endpoints via **Actuator**.

---
