# ForgeAI: System Architecture & Data Flow

This document details the architectural design, security model, and data flow pipelines of ForgeAI.

---

## 1. High-Level System Architecture

```mermaid
graph TB
    subgraph "Client Tier (Next.js 14 / React 18)"
        A[Browser / Developer] -->|UI Interactions| B[Next.js App Router]
        B --> C[AuthContext]
        B --> D[Domain API Services]
        D -->|Axios/Fetch with Bearer JWT| E[Centralized API Client]
    end

    subgraph "Application Tier (Spring Boot 3.3)"
        E -->|HTTP REST Requests| F[Security Filter Chain]
        F --> G[JwtAuthenticationFilter]
        G --> H[Spring REST Controllers]
        H --> I[Business Services Layer]
        
        subgraph "Service Layer"
            I --> J[ScoreCalculationService]
            I --> K[SkillGapService]
            I --> L[RoadmapService]
            I --> M[MissionService]
            I --> N[DebuggingService]
            I --> O[CodeReviewService]
            I --> P[InterviewService]
            I --> Q[GithubService]
        end
    end

    subgraph "Data Tier"
        I --> R[(PostgreSQL / H2 Database)]
        I --> S[(Redis / Caffeine Cache)]
    end

    subgraph "External Providers"
        Q -->|REST API with Token| T[GitHub API]
        O -->|LLM REST / Offline Fallback| U[AI Provider]
    end
```

---

## 2. Authentication & Security Architecture
- **Stateless Session Management:** Sessions are never stored in server memory (`SessionCreationPolicy.STATELESS`).
- **Cryptographic Signature:** JWT tokens are signed using HMAC-SHA256 (`app.jwt.secret`).
- **Token Claims:** Include subject (`userId`), `email`, `name`, issued date, and 24-hour expiration.
- **Filter Interception:** `JwtAuthenticationFilter` intercepts incoming requests, validates the signature, extracts the subject, and constructs a `UsernamePasswordAuthenticationToken` in `SecurityContextHolder`.
- **CORS Configuration:** Configured to allow Next.js (`http://localhost:3000`) with standard headers and credentials.

---

## 3. The Closed-Loop Engineering Growth Engine
ForgeAI enforces a continuous feedback cycle:

```mermaid
sequenceDiagram
    autonumber
    actor Dev as Student / Developer
    participant UI as Next.js Dashboard
    participant API as Spring Boot API
    participant Engine as Score & Gap Engine
    participant DB as Relational Database

    Dev->>UI: Submits Code / Takes Assessment / Syncs GitHub
    UI->>API: POST /api/assessments/submit or /api/github/sync
    API->>DB: Persist Submission Outcome
    API->>Engine: Trigger calculateAndSaveScores(userId)
    Engine->>DB: Query Assessment Results, Missions, Repositories
    Engine->>Engine: Compute 10-Dimension Weighted Score & Readiness
    Engine->>Engine: Identify Deficits below 75.0 (High/Med/Low)
    Engine->>DB: Persist EngineeringScore & SkillGaps
    API-->>UI: Return Updated ScoreDto & Next Focus Area
    UI-->>Dev: Update Dashboard Dials & Highlight Priority Action
```

---

## 4. Multi-Language Code Review Architecture
The Code Review subsystem operates under a dual-mode design:
1. **Live LLM Integration:** If `AI_API_KEY` is present, the backend issues an asynchronous REST call to OpenAI or Gemini endpoints.
2. **Offline Heuristic AST Engine:** If unconfigured or in offline viva mode, a built-in static analyzer parses the syntax for:
   - Exception handling containment (`try/catch`).
   - Hardcoded secrets and credential tokens.
   - Dynamic SQL string concatenations (`SQL Injection`).
   - Algorithmic time-space scaling ($O(N^2)$ nested loops).
   - Generates idiomatic refactored code across Java, Python, TypeScript, Go, Rust, C++, and SQL.

---

## 5. Deployment Topology
ForgeAI can be deployed in two configurations:
1. **Local Demonstration Mode:** Default `h2` profile with zero external installation requirements.
2. **Production Containerized Stack:** Managed via `docker-compose.yml` orchestrating:
   - `forgeai-frontend` (Port 3000)
   - `forgeai-backend` (Port 8080)
   - `forgeai-postgres` (Port 5432)
   - `forgeai-redis` (Port 6379)
