package com.forgeai.config;

import com.forgeai.entity.*;
import com.forgeai.repository.*;
import com.forgeai.service.ScoreCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// Automatically seeds realistic engineering sample data, assessments, missions, and demo accounts on application boot.
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final DebuggingChallengeRepository debuggingChallengeRepository;
    private final MissionRepository missionRepository;
    private final ProjectRepository projectRepository;
    private final RepositoryEntityRepository repositoryEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScoreCalculationService scoreCalculationService;

    public DataInitializer(UserRepository userRepository,
                           AssessmentRepository assessmentRepository,
                           QuestionRepository questionRepository,
                           DebuggingChallengeRepository debuggingChallengeRepository,
                           MissionRepository missionRepository,
                           ProjectRepository projectRepository,
                           RepositoryEntityRepository repositoryEntityRepository,
                           PasswordEncoder passwordEncoder,
                           ScoreCalculationService scoreCalculationService) {
        this.userRepository = userRepository;
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.debuggingChallengeRepository = debuggingChallengeRepository;
        this.missionRepository = missionRepository;
        this.projectRepository = projectRepository;
        this.repositoryEntityRepository = repositoryEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Executes on startup to populate database tables with standard engineering curriculum and demo accounts.
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            logger.info("Initializing database with ForgeAI sample curriculum and demo accounts...");

            // 1. Create Demo Engineer Account
            User demoUser = new User("Alex Morgan (Demo Engineer)", "demo@forgeai.dev",
                    passwordEncoder.encode("Demo1234!"), "demo-engineer");
            demoUser.setBio("Sample Engineering Student Profile (Final-Year Viva Demonstration Mode)");
            demoUser.setXp(2450);
            demoUser.setLevel(4);
            demoUser.setStreak(7);
            User savedDemoUser = userRepository.save(demoUser);

            // 2. Seed Engineering Assessments & Questions
            seedAssessments();

            // 3. Seed Interactive Debugging Challenges (Multi-Language: Python, TypeScript, Java, Go)
            seedDebuggingChallenges();

            // 4. Seed Hands-on Engineering Missions
            seedMissions();

            // 5. Seed Portfolio Projects
            seedProjects(savedDemoUser);

            // 6. Seed Sample GitHub Repositories
            seedRepositories(savedDemoUser);

            // 7. Calculate Baseline Engineering Score
            scoreCalculationService.calculateAndSaveScores(savedDemoUser.getId());

            logger.info("Database initialization completed successfully!");
        }
    }

    // Populates technical assessments across the 8 core engineering disciplines.
    private void seedAssessments() {
        Assessment prog = assessmentRepository.save(new Assessment(
                "Core Programming & Data Structures", "Programming", "Intermediate", 15,
                "Evaluates algorithmic efficiency, OOP patterns, and modern language memory models."
        ));

        questionRepository.save(new Question(prog,
                "Which data structure provides amortized O(1) time complexity for both key-value lookups and insertion?",
                "Programming", "Intermediate",
                "[\"Binary Search Tree\", \"Hash Map\", \"Linked List\", \"Sorted Array\"]",
                1,
                "Hash Maps use hash functions to index into buckets, delivering O(1) amortized performance."));

        questionRepository.save(new Question(prog,
                "In Java, what occurs when a thread calls `wait()` on an object?",
                "Programming", "Intermediate",
                "[\"The thread yields CPU time without releasing locks\", \"The thread releases the object monitor and enters WAITING state\", \"The thread terminates\", \"The thread throws IllegalThreadStateException\"]",
                1,
                "`wait()` causes the current thread to release monitor ownership and wait until another thread invokes `notify()` or `notifyAll()`."));

        Assessment test = assessmentRepository.save(new Assessment(
                "Automated Testing & Defect Prevention", "Testing", "Intermediate", 15,
                "Evaluates unit test isolation, mocking methodologies, and code coverage best practices."
        ));

        questionRepository.save(new Question(test,
                "What is the primary purpose of using test mocks (e.g., Mockito) in unit tests?",
                "Testing", "Intermediate",
                "[\"To speed up database indexing\", \"To isolate the class under test from external dependencies\", \"To test network latency\", \"To replace unit tests with integration tests\"]",
                1,
                "Mocks simulate external dependency behavior so unit tests verify only the target unit's business logic."));

        Assessment sys = assessmentRepository.save(new Assessment(
                "Distributed System Design & Scaling", "System Design", "Intermediate", 20,
                "Evaluates caching strategies, database sharding, and high-availability architecture."
        ));

        questionRepository.save(new Question(sys,
                "In a distributed cache-aside pattern, what happens on a cache read miss?",
                "System Design", "Intermediate",
                "[\"The cache writes null to the database\", \"The application queries the database, writes the result to cache, and returns it\", \"The cache returns an error 500\", \"The database updates the cache asynchronously without returning\"]",
                1,
                "In Cache-Aside, the application fetches data from the database upon a cache miss, populates the cache for subsequent queries, and returns the data."));
    }

    // Populates realistic debugging challenges across multiple languages (Python, TypeScript, Java, Go).
    private void seedDebuggingChallenges() {
        // Python Challenge
        debuggingChallengeRepository.save(new DebuggingChallenge(
                "Python Mutable Default Argument Pitfall",
                "A student function logging user actions unexpectedly appends all user events into the same shared list across separate calls.",
                "Python", "Easy",
                "def track_event(event_name: str, tags: list = []):\n" +
                "    # Bug: Default mutable list argument shared across all invocations\n" +
                "    tags.append(event_name)\n" +
                "    return tags",
                "def track_event(event_name: str, tags: list = None):\n" +
                "    if tags is None:\n" +
                "        tags = []\n" +
                "    tags.append(event_name)\n" +
                "    return tags",
                "Default arguments in Python are evaluated once at function definition, not per call. Using a mutable list `[]` as default creates a shared singleton across calls.",
                "[\"Inspect when Python evaluates default function parameters\", \"Use None as default and initialize inside function body\"]",
                "Calling track_event('login') then track_event('signup') should return isolated single-element lists.",
                100
        ));

        // TypeScript / JavaScript Challenge
        debuggingChallengeRepository.save(new DebuggingChallenge(
                "Async Loop Callback Race Condition",
                "An order processing loop using `Array.prototype.forEach` fails to await asynchronous payment authorizations before completing.",
                "TypeScript", "Medium",
                "async function processOrders(orders: Order[]): Promise<void> {\n" +
                "  // Bug: forEach does not await promises returned by its callback\n" +
                "  orders.forEach(async (order) => {\n" +
                "    await authorizePayment(order.id);\n" +
                "    console.log(`Paid: ${order.id}`);\n" +
                "  });\n" +
                "  console.log('All orders processed!');\n" +
                "}",
                "async function processOrders(orders: Order[]): Promise<void> {\n" +
                "  for (const order of orders) {\n" +
                "    await authorizePayment(order.id);\n" +
                "    console.log(`Paid: ${order.id}`);\n" +
                "  }\n" +
                "  // Or: await Promise.all(orders.map(order => authorizePayment(order.id)));\n" +
                "  console.log('All orders processed!');\n" +
                "}",
                "`Array.prototype.forEach` ignores returned promises. The function exits before asynchronous tasks complete. Use `for...of` or `Promise.all`.",
                "[\"Does Array.prototype.forEach await async callbacks?\", \"Consider a standard for...of loop or Promise.all\"]",
                "processOrders([order1, order2]) must block until all authorizations finish.",
                120
        ));

        // Java Challenge
        debuggingChallengeRepository.save(new DebuggingChallenge(
                "Off-By-One Array Boundary Exception",
                "A sliding window average calculation throws `ArrayIndexOutOfBoundsException` on boundary conditions.",
                "Java", "Easy",
                "public double calculateWindowAverage(int[] arr, int k) {\n" +
                "    int sum = 0;\n" +
                "    for (int i = 0; i <= k; i++) {\n" +  // Bug: <= instead of <
                "        sum += arr[i];\n" +
                "    }\n" +
                "    return (double) sum / k;\n" +
                "}",
                "public double calculateWindowAverage(int[] arr, int k) {\n" +
                "    if (arr == null || arr.length < k || k <= 0) return 0.0;\n" +
                "    int sum = 0;\n" +
                "    for (int i = 0; i < k; i++) {\n" +
                "        sum += arr[i];\n" +
                "    }\n" +
                "    return (double) sum / k;\n" +
                "}",
                "The loop condition `i <= k` iterates k+1 times, exceeding the k-element window and causing an index out of bounds.",
                "[\"Notice the comparison operator in the for-loop header\", \"Count how many elements are added to sum when k=3\"]",
                "Inputs: arr=[10, 20, 30, 40], k=3 -> Expected output: 20.0",
                100
        ));

        // Go Challenge
        debuggingChallengeRepository.save(new DebuggingChallenge(
                "Go Goroutine Closure Variable Capture",
                "A task dispatcher launching concurrent goroutines prints identical worker IDs because loop variable is captured by reference.",
                "Go", "Hard",
                "func launchWorkers(ids []int) {\n" +
                "    for _, id := range ids {\n" +
                "        go func() {\n" +
                "            fmt.Printf(\"Worker ID: %d\\n\", id) // Bug: id captured by reference\n" +
                "        }()\n" +
                "    }\n" +
                "}",
                "func launchWorkers(ids []int) {\n" +
                "    for _, id := range ids {\n" +
                "        go func(workerID int) {\n" +
                "            fmt.Printf(\"Worker ID: %d\\n\", workerID)\n" +
                "        }(id) // Pass by value parameter\n" +
                "    }\n" +
                "}",
                "In Go versions prior to 1.22, loop variables are reused across iterations. All goroutines read the same memory address after loop completion. Pass `id` as parameter.",
                "[\"How does Go closure capture loop variables in goroutines?\", \"Pass the loop variable as an explicit argument to the goroutine\"]",
                "IDs [1, 2, 3, 4] must each be printed without duplication.",
                180
        ));
    }

    // Populates practical hands-on engineering missions.
    private void seedMissions() {
        missionRepository.save(new Mission(
                "Add Unit & Integration Tests to REST API",
                "Write automated test coverage using JUnit 5 and Mockito for user authentication and score calculation endpoints.",
                "Testing", "Medium", 150,
                "Minimum 80% line coverage||Verify boundary conditions||Mock database repositories",
                "All test cases pass green in `mvn test`"
        ));

        missionRepository.save(new Mission(
                "Implement Redis Caching for Engineering Scores",
                "Introduce a Redis caching layer around score calculation to prevent redundant database computations.",
                "System Design", "Medium", 180,
                "Configure Spring Cache with Redis||Set 1-hour TTL||Implement cache eviction on assessment submission",
                "Score query latency reduced by >70% on cached reads"
        ));

        missionRepository.save(new Mission(
                "Mitigate OWASP Top 10 Security Vulnerabilities",
                "Audit the application against SQL injection, XSS, and enforce strict CORS and JWT secret rotation.",
                "Security", "Hard", 200,
                "Verify parameterized JPA queries||Enable strict CSP headers||Validate all user input payloads",
                "Zero high/critical security alerts reported in security audits"
        ));

        missionRepository.save(new Mission(
                "Refactor Controller to Global Exception Handling",
                "Centralize error handling with `@RestControllerAdvice` and structured HTTP error responses.",
                "Programming", "Easy", 120,
                "Replace inline try-catches with global handler||Return standardized ErrorResponse JSON||Support 400, 401, 404, 500",
                "Structured error responses verified across all REST endpoints"
        ));
    }

    // Seeds student portfolio projects.
    private void seedProjects(User user) {
        projectRepository.save(new Project(user, "ForgeAI Engineering Platform",
                "Full-stack AI-driven developer growth platform calculating transparent engineering readiness scores.",
                "Next.js, TypeScript, Spring Boot, PostgreSQL, Tailwind CSS",
                "https://github.com/ashish-dhakane/forgeai-platform",
                "http://localhost:3000",
                "Advanced"));

        projectRepository.save(new Project(user, "Distributed Asynchronous Job Scheduler",
                "High-throughput task worker pool utilizing Redis streams and Java concurrency primitives.",
                "Java 21, Spring Boot, Redis, Docker",
                "https://github.com/ashish-dhakane/distributed-task-worker",
                "http://localhost:8080/swagger-ui.html",
                "Advanced"));
    }

    // Seeds GitHub repository snapshot records for demo user.
    private void seedRepositories(User user) {
        repositoryEntityRepository.save(new RepositoryEntity(user, "forgeai-platform",
                "ashish-dhakane/forgeai-platform",
                "AI-Powered Engineering Growth Platform for Developing Industry-Ready Software Engineers",
                "TypeScript", 18, 5, "https://github.com/ashish-dhakane/forgeai-platform", 88.0,
                "Next.js, TypeScript, Spring Boot, PostgreSQL, Tailwind CSS, Recharts",
                "Clean modular architecture||Comprehensive REST API endpoints||Stateless JWT security",
                "Add automated integration test suite||Implement Redis cache-aside tier"));

        repositoryEntityRepository.save(new RepositoryEntity(user, "distributed-task-worker",
                "ashish-dhakane/distributed-task-worker",
                "Fault-tolerant asynchronous task scheduler built with Java, Redis, and Spring Web",
                "Java", 24, 8, "https://github.com/ashish-dhakane/distributed-task-worker", 92.0,
                "Java 21, Spring Boot, Redis, JUnit 5, Docker",
                "High concurrency resilience||Clean modular thread management||Comprehensive unit tests",
                "Add OpenTelemetry distributed tracing||Introduce dead-letter queue recovery"));
    }
}
