<img width="1114" height="666" alt="image" src="https://github.com/user-attachments/assets/4a618b7f-adf4-43e9-a4c5-cbb73c319544" />

# Core Banking System (MVP)

A minimal microservices-based banking system demonstrating **account**, **transaction**, and **ledger** management with event-driven communication.

---

## Services

- **Account Service** – Manages bank accounts  
- **Transaction Service** – Handles transfers and payments  
- **Ledger Service** – Records double-entry bookkeeping  
- **Identity Service** – Authentication & authorization  
- **Notification Service** – Sends alerts via RabbitMQ  
- **Gateway** – Single entry point for clients  
- **Discovery Service** – Service registration & discovery  
- **DLQ** – Dead letter queue for failed events  

---

## Stack

- **Spring Boot**  
- **Kafka** (events)  
- **RabbitMQ** (notifications)  
- **Eureka** (discovery)  
- **API Gateway**  
- **Docker Compose**  

---

## Run

```bash
docker-compose up -d
