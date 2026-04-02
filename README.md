# Nexus (Distributed Rate Limiter)

## 1. Project Overview
**Nexus** is a high-performance distributed rate-limiting library designed to protect microservices from cascading failures and traffic spikes. Unlike local rate limiters, Nexus ensures that a user’s quota is enforced globally across all instances of a service.

### Problem Statement
In a distributed environment, local (in-memory) rate limiting is insufficient. If a user is limited to 10 requests/min and you have 5 service instances, the user could potentially make 50 requests/min. Nexus solves this by externalizing the state to a distributed store (Redis).

---

## 2. System Architecture

### High-Level Components
1.  **Nexus Core Library (Java):** An AOP-based (Aspect Oriented Programming) library that developers include in their Spring Boot applications.
2.  **Distributed Store (Redis):** Stores the current state of "buckets" (token counts and timestamps).
3.  **Nexus Admin Server (Java/Spring):** A management layer that provides APIs to update rate-limit configurations in Redis.
4.  **Nexus Dashboard (React):** A UI to visualize active limits and adjust them in real-time.

### Data Flow
1.  **Request Arrival:** A request hits an API endpoint annotated with `@NexusLimit`.
2.  **Intercept:** The library intercepts the call and extracts the key (e.g., User ID or IP).
3.  **Redis Lua Script:** The library executes a Lua script in Redis to check/decrement tokens atomically.
4.  **Decision:** 
    *   If tokens available: Request proceeds.
    *   If no tokens: Returns `429 Too Many Requests`.

---

## 3. Core Algorithm: Token Bucket
Nexus uses the **Token Bucket** algorithm because it allows for "burstiness" while maintaining a steady long-term rate.

*   **Bucket Capacity ($C$):** The maximum number of tokens a bucket can hold.
*   **Refill Rate ($R$):** How many tokens are added per second.
*   **Logic:** 
    *   Each request costs 1 token.
    *   Tokens are added over time based on the delta between the last request and the current request.
    *   `NewTokens = Min(Capacity, CurrentTokens + (TimePassed * RefillRate))`

---

## 4. Technical Implementation Details

### A. The Java Library (Backend)
*   **Technology:** Java 17+, Spring Boot Starter, Spring AOP.
*   **Redis Integration:** Use **Lettuce** or **Jedis**. To prevent race conditions (Read-Modify-Write), use **Redis Lua Scripts**.
    *   *Why Lua?* Lua scripts run atomically in Redis. No two instances can interfere with the token calculation for the same key.

### B. The Admin Panel (Frontend)
*   **Technology:** React, Tailwind CSS, Axios.
*   **Features:**
    *   **Configuration Management:** A table view showing all active rate-limiting rules.
    *   **Dynamic Updates:** Change a `refreshRate` from 5 to 10 req/sec on the fly.
    *   **Persistence:** The Admin Panel saves these configs to Redis, and the Library reads the latest config from Redis on every request (or caches it for 1 second).

---

## 5. Database Schema (Redis Key Design)

1.  **State Key:** `nexus:state:{api_name}:{user_id}`
    *   *Type:* Hash
    *   *Fields:* `tokens` (current count), `last_updated` (timestamp).
2.  **Config Key:** `nexus:config:{api_name}`
    *   *Type:* Hash
    *   *Fields:* `capacity`, `refresh_rate`.

---

## 6. API Design (Admin Server)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/configs` | Fetch all active rate limit configurations. |
| `POST` | `/api/v1/configs` | Create or Update a rate limit rule. |
| `DELETE` | `/api/v1/configs/{id}` | Remove a rate limit rule. |

---

## 7. Performance & Scalability Considerations

*   **Latency:** Every API call now requires a network trip to Redis. To mitigate this, ensure Redis is in the same VPC/network. Lua scripts keep the logic server-side in Redis to minimize round-trips.
*   **Redis Failure:** If Redis is down, the library should "fail open" (allow requests) to ensure availability, while logging an error.
*   **Clock Drift:** In a distributed system, timestamps might differ. Redis `TIME` command should be used to get a consistent global timestamp for token replenishment.

---

## 8. Development Roadmap (Action Plan)

1.  **Phase 1 (Core):** Write the Lua script for Token Bucket. Implement a simple Java class that calls this script.
2.  **Phase 2 (Library):** Create the `@NexusLimit` annotation and use Spring AOP (`@Around` advice) to trigger the logic.
3.  **Phase 3 (Management):** Create a Spring Boot REST controller to modify the Redis config keys.
4.  **Phase 4 (Frontend):** Build the React dashboard to call the REST controller.
5.  **Phase 5 (Testing):** Use **JMeter** or **Locust** to simulate 100+ concurrent users and verify that the rate limiter holds steady.

---