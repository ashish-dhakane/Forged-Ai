# FORGEAI: Engineering Growth Platform
> **“An AI-Powered Engineering Growth Platform for Developing Industry-Ready Software Engineers”**  
> *Final-Year Computer Science Major Project*

---

## 1. Project Overview
**ForgeAI** is a developer growth platform designed to bridge the critical gap between academic computer science curricula and modern software engineering expectations. Rather than acting as a generic quiz app or a passive chatbot, ForgeAI analyzes a student's real development work across GitHub, evaluates diagnostic debugging capabilities, delivers tailored engineering missions, and produces a transparent, deterministic **Engineering Score** (0–100) and **Industry Readiness Index**.

---

## 2. Problem Statement
Traditional computer science education primarily tests rote algorithmic memorization and theoretical syntax. However, software engineering teams hire candidates based on **defensive architecture, debugging efficiency, automated test coverage, concurrency safety, clean documentation, and code review maturity**. Students often lack an actionable mechanism to:
1. Know their exact competency level across real-world engineering dimensions.
2. Identify specific deficits in testing, security, or system design.
3. Follow an adaptive learning roadmap with measurable milestones.

---

## 3. Objectives
- **Transparent Scoring:** Deterministically calculate an Engineering Score from 10 weighted dimensions without arbitrary AI hallucination.
- **Continuous Feedback Loop:** Establish the engineering cycle:  
  $$\text{Data Collection} \longrightarrow \text{Analysis} \longrightarrow \text{Skill Gaps} \longrightarrow \text{Roadmap} \longrightarrow \text{Missions} \longrightarrow \text{Evaluation} \longrightarrow \text{Growth}$$
- **Multi-Language Support:** Static analysis, code review, and debugging across **Java, Python, TypeScript, JavaScript, Go, Rust, C++, C, SQL, and C#**.
- **Viva-Ready Reliability:** Provide an instant **Demo Mode** with realistic engineering profiles so the platform functions under university presentation conditions with zero external dependencies.

---

## 4. Features
| Feature Module | Description |
| :--- | :--- |
| **Dashboard** | Dual dials for Engineering Score (72/100) and Industry Readiness (68/100), XP/Streak metrics, Recharts radar snapshot, and dynamic priority focus card. |
| **Engineering Profile** | 6-dimension **Engineer DNA** (Builder, Debugger, Problem Solver, Architect, Security, Communicator) with qualitative AI synthesis and portfolio links. |
| **GitHub Intelligence** | Connects to GitHub API to profile language distributions, commit velocity, and automated repository health audits with actionable improvement points. |
| **Skill Assessment** | Interactive assessments across 8 engineering disciplines with multiple-choice and conceptual questions, instant tier classification, and XP rewards. |
| **Skill Gap Analysis** | Compares current performance with industry readiness thresholds, highlighting High, Medium, and Low severity gaps with prescriptive action items. |
| **Personalized Roadmap** | 5-phase adaptive engineering curriculum with milestone tracking, progress sliders, and curated technical reading resources. |
| **Engineering Missions** | Hands-on production assignments (e.g. Unit testing, Redis caching, OWASP defense) with submission notes and XP claiming. |
| **AI Code Review** | Multi-language code editor with AST and heuristic audits detecting SQL injection, hardcoded secrets, algorithmic complexity, and offering refactored snippets. |
| **Debugging Engine** | Hands-on puzzle environment across Python, TypeScript, Java, and Go evaluating both the student's root-cause explanation and code fix. |
| **AI Technical Interview** | Role-tailored interview simulations (Software Engineer, Backend, Frontend, Full Stack) evaluating technical depth, communication, and problem solving. |
| **Progress Analytics** | Longitudinal graphs with 7d, 30d, and 90d filters tracking score trajectory, consistency percentages, and XP velocity. |
| **Platform Settings** | Profile metadata controls, optional GitHub PAT and AI API key management, and Demo Mode toggle. |

---

## 5. System Architecture
```
                                ┌──────────────────────────────┐
                                │   Next.js 14 Frontend UI     │
                                │   (React 18, Tailwind, Recharts)│
                                └──────────────┬───────────────┘
                                               │ HTTP / REST (JWT)
                                               ▼
                                ┌──────────────────────────────┐
                                │ Spring Boot 3.3 Backend API  │
                                │ (Spring Security, Stateless) │
                                └──────┬───────────────┬───────┘
                     ┌─────────────────┘               └────────────────┐
                     ▼                                                  ▼
     ┌──────────────────────────────┐                   ┌──────────────────────────────┐
     │  PostgreSQL / H2 Database    │                   │   External Integrations      │
     │  (Entities, Repositories,    │                   │   - GitHub REST API          │
     │   JPA / Hibernate ORM)       │                   │   - AI Service (LLM/Mock)    │
     └──────────────────────────────┘                   └──────────────────────────────┘
```

---

## 6. Technology Stack
- **Frontend:** Next.js 14.2 (App Router), React 18, TypeScript, Tailwind CSS, Recharts, Lucide React.
- **Backend:** Java 21 / 25, Spring Boot 3.3.3, Spring Web, Spring Security, Spring Data JPA, Hibernate, JJWT 0.12.6.
- **Databases:** PostgreSQL 16 (production profile), H2 In-Memory (default out-of-the-box viva profile).
- **Caching:** Redis 7 (via Docker) / local caching abstractions.
- **DevOps:** Multi-stage Dockerfiles, Docker Compose, GitHub Actions CI.

---

## 7. Folder Structure
```
forgeai/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/java/com/forgeai/
│       │   ├── ForgeAiApplication.java
│       │   ├── config/              # SecurityConfig, DataInitializer, CorsConfig
│       │   ├── controller/          # 13 REST Controllers (Auth, Dashboard, Github, etc.)
│       │   ├── dto/                 # Request & Response Data Transfer Objects
│       │   ├── entity/              # 19 JPA Database Entities
│       │   ├── exception/           # GlobalExceptionHandler & ErrorResponse
│       │   ├── repository/          # 19 Spring Data JPA Repositories
│       │   ├── security/            # JwtTokenProvider, JwtAuthenticationFilter
│       │   └── service/             # ScoreCalculationService, SkillGapService, etc.
│       └── main/resources/
│           └── application.yml      # Dual H2 & PostgreSQL configuration profiles
├── frontend/
│   ├── package.json
│   ├── tsconfig.json
│   ├── tailwind.config.ts
│   ├── Dockerfile
│   ├── app/
│   │   ├── layout.tsx               # Root layout & AuthProvider
│   │   ├── page.tsx                 # Public Landing Page
│   │   ├── login/page.tsx           # Sign-in & 1-Click Demo Login
│   │   ├── register/page.tsx        # Registration
│   │   └── (authenticated)/         # Authenticated workspace layout & 12 modules
│   ├── components/
│   │   ├── layout/                  # Sidebar, Header, DemoBadge
│   │   └── ui/                      # StatCard, ProgressBar, etc.
│   ├── context/                     # AuthContext.tsx
│   ├── services/                    # api.ts, dashboardService.ts, etc.
│   └── types/                       # TypeScript interfaces
├── .github/workflows/ci.yml         # CI pipeline
├── docker-compose.yml               # Multi-container stack (Postgres, Redis, App)
├── README.md                        # Project documentation
└── ARCHITECTURE.md                  # Detailed data flow diagrams
```

---

## 8. Database Schema & Relationships
The backend models 19 relational entities via JPA/Hibernate:
1. `users`: Credentials, GitHub handle, XP, Level, Streak, Role.
2. `engineering_scores`: 10 category metrics, readiness subscores, priority focus area.
3. `skills`: User competencies with proficiency levels.
4. `assessments` & `questions`: Technical tests with options, correct answer index, explanations.
5. `assessment_results`: Candidate scores, correct counts, performance tier (`Strong`, `Average`, `Needs Improvement`).
6. `skill_gaps`: Deficits comparing current level with target benchmark and action items.
7. `roadmaps` & `roadmap_items`: 5-phase learning roadmap with status and progress percentage.
8. `missions` & `mission_submissions`: Hands-on engineering assignments with verification notes.
9. `code_reviews`: Submitted snippets, language, quality score, security vulnerabilities, refactoring.
10. `debugging_challenges` & `debugging_attempts`: Multi-language buggy code, hints, student explanations.
11. `interviews`, `interview_questions`, `interview_answers`: Simulated interview transcripts and rubrics.
12. `repositories` & `projects`: Synced GitHub repositories and student project portfolio.

---

## 9. API Documentation
All endpoints return standard JSON responses and require `Authorization: Bearer <token>` (except public auth routes).

### Authentication (`/api/auth`)
- `POST /api/auth/register` — Registers student account.
- `POST /api/auth/login` — Authenticates credentials and returns JWT token.
- `POST /api/auth/demo-login` — 1-Click authentication loading pre-seeded demo engineer profile.
- `GET /api/auth/me` — Returns authenticated user profile.

### Dashboard & Analytics (`/api/dashboard`, `/api/analytics`)
- `GET /api/dashboard/overview` — Retrieves composite Engineering Score, Readiness, and category breakdown.
- `GET /api/dashboard/focus` — Retrieves prioritized focus recommendation and rationale.
- `GET /api/analytics/trends?period=7d|30d|90d` — Returns score trajectory and XP velocity.

### GitHub Intelligence (`/api/github`)
- `GET /api/github/repos` — Retrieves user's synced repositories.
- `POST /api/github/sync` — Synchronizes with GitHub API (or realistic fallback if rate-limited).
- `GET /api/github/snapshot` — Aggregates language distribution and commit activity.

### Assessments & Skill Gaps (`/api/assessments`, `/api/skill-gaps`)
- `GET /api/assessments` — Lists assessment tests.
- `POST /api/assessments/submit` — Evaluates candidate answers and updates scores.
- `GET /api/skill-gaps` — Lists identified competency gaps.
- `POST /api/skill-gaps/recalculate` — Recomputes gaps based on latest work.

### Missions, Debugging & Code Review (`/api/missions`, `/api/debugging`, `/api/code-review`)
- `GET /api/missions` — Lists engineering missions.
- `POST /api/missions/{id}/submit` — Submits mission verification and awards XP.
- `GET /api/debugging/challenges` — Lists multi-language buggy puzzles.
- `POST /api/debugging/challenges/{id}/submit` — Evaluates student diagnosis and code fix.
- `POST /api/code-review/analyze` — Analyzes code for quality, bugs, security, and refactoring.

### AI Interview (`/api/interview`)
- `POST /api/interview/start` — Generates role-specific interview round.
- `POST /api/interview/answer` — Submits answer to a question.
- `POST /api/interview/{id}/evaluate` — Generates comprehensive scorecard.

---

## 10. Environment Variables
### Backend (`backend/src/main/resources/application.yml`)
```bash
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=h2          # 'h2' for zero-dependency demo | 'postgres' for production
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/forgeaidb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
APP_JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
APP_JWT_EXPIRATION_MS=86400000     # 24 hours
GITHUB_TOKEN=                      # Optional: increases GitHub API rate limit
AI_API_KEY=                        # Optional: OpenAI or Gemini key (offline analyzer works without key)
```

### Frontend (`frontend/.env.local`)
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## 11. Installation & Running Steps

### Prerequisites
- Java 21 or higher installed (`java -version`)
- Node.js 18 or higher installed (`node -v`, `npm -v`)
- Apache Maven (or use Maven wrapper/provided binary)

### Running the Backend
```bash
cd backend
# Using Maven:
mvn spring-boot:run
# Backend will start on http://localhost:8080 (auto-seeds database with demo data)
```

### Running the Frontend
```bash
cd frontend
npm install
npm run dev
# Frontend will start on http://localhost:3000
```

### Accessing the Platform
Open your browser to: **`http://localhost:3000`**  
Click **"Viva 1-Click Demo"** or **"Login as Demo Engineer"** for instant evaluation access!

---

## 12. Running with PostgreSQL & Redis (Docker Setup)
If you wish to demonstrate containerized production deployment with PostgreSQL and Redis:
```bash
# From the project root:
docker-compose up --build
```
- PostgreSQL will be accessible on port `5432`
- Redis will be accessible on port `6379`
- Spring Boot Backend on port `8080`
- Next.js Frontend on port `3000`

---

## 13. Demo Mode & Viva Demonstration Instructions
During your final-year viva, external networks or third-party APIs can be unpredictable. ForgeAI includes an **automatic demonstration safety architecture**:
1. **Default Database:** Runs in-memory H2 with PostgreSQL syntax compatibility. On startup, `DataInitializer` seeds:
   - Account: `demo@forgeai.dev` (Password: `Demo1234!`)
   - 3 Engineering Assessments with 5 technical questions
   - 4 Multi-Language Debugging Puzzles (Python, TypeScript, Java, Go)
   - 4 Practical Engineering Missions
   - 2 Portfolio Projects and 3 GitHub Repositories
   - Pre-calculated Engineering Score (72.0/100) and Focus Area
2. **GitHub Fallback:** If unauthenticated or GitHub rate limits occur, authentic sample repositories are rendered with a subtle `DEMO DATA` badge.
3. **AI Fallback:** If no `AI_API_KEY` is present, the AST and heuristic analysis engine performs offline pattern matching (SQL injection checks, hardcoded credentials, nested loops, error containment).

---

## 14. How Engineering Score Works
The Engineering Score is calculated **deterministically** using verified weights summing to 100%:
$$\text{Score} = (0.20 \times \text{Prog}) + (0.15 \times \text{PS}) + (0.15 \times \text{Projects}) + (0.10 \times \text{GitHub}) + (0.10 \times \text{Debugging}) + (0.08 \times \text{Testing}) + (0.08 \times \text{SysDes}) + (0.05 \times \text{Security}) + (0.05 \times \text{Comm}) + (0.04 \times \text{Consistency})$$

- **Prog / PS:** Sourced from assessment results.
- **Debugging:** Calculated from the success ratio of diagnostic puzzle attempts.
- **Testing:** Sourced from completed unit testing missions.
- **GitHub / Projects:** Derived from repository health score and tech stack complexity.
- **Consistency:** Calculated from daily streak continuity.

---

## 15. How Skill Gap Detection Works
`SkillGapService` compares the student's category proficiency against the standard industry benchmark ($75.0/100$):
- **High Severity Gap ($<60.0$):** High-priority deficit requiring immediate hands-on missions (e.g. Automated Unit Testing).
- **Medium Severity Gap ($60.0 - 74.0$):** Competency exists but lacks architectural depth (e.g. Concurrency Profiling, Redis Caching).
- **Low Severity ($75.0+$):** Strong baseline requiring only continuous maintenance.

---

## 16. How AI Code Review Works
1. Student submits code and selects language (Java, Python, TypeScript, Go, C++, SQL, etc.).
2. The backend inspects the code for:
   - Exception handling and error propagation.
   - Security hazards (SQL concatenation, hardcoded secrets).
   - Algorithmic complexity (nested loops, quadratic scaling).
3. Generates educational feedback and an **idiomatic refactored version** with clean comments.

---

## 17. Future Improvements
- Integration with live container sandboxes (e.g. Judge0) for automated execution of test suites.
- Webhook listeners for real-time GitHub pull request code reviews.
- WebSocket-powered audio speech-to-text during mock interview simulations.

---

## License & University Defense Notice
Developed as a Computer Science Final-Year Major Project by Ashish Dhakane. All code is original, modular, and designed for educational evaluation.
