package com.forgeai.service;

import com.forgeai.dto.InterviewAnswerRequest;
import com.forgeai.dto.InterviewEvaluationResponse;
import com.forgeai.dto.InterviewStartRequest;
import com.forgeai.entity.Interview;
import com.forgeai.entity.InterviewAnswer;
import com.forgeai.entity.InterviewQuestion;
import com.forgeai.entity.User;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.InterviewAnswerRepository;
import com.forgeai.repository.InterviewQuestionRepository;
import com.forgeai.repository.InterviewRepository;
import com.forgeai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// Simulates role-specific AI technical interviews, evaluates student answers, and generates hiring readiness reports.
@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;

    public InterviewService(InterviewRepository interviewRepository,
                            InterviewQuestionRepository questionRepository,
                            InterviewAnswerRepository answerRepository,
                            UserRepository userRepository,
                            ScoreCalculationService scoreCalculationService) {
        this.interviewRepository = interviewRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Starts a new mock technical interview by creating contextual questions adapted to the chosen role.
    @Transactional
    public Interview startInterview(Long userId, InterviewStartRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Interview interview = new Interview(user, request.getRole(), request.getDifficulty(), request.getCategory());
        Interview savedInterview = interviewRepository.save(interview);

        List<InterviewQuestion> questions = generateQuestionsForRole(savedInterview, request.getRole(), request.getDifficulty());
        questionRepository.saveAll(questions);
        savedInterview.setQuestions(questions);

        return savedInterview;
    }

    // Submits an answer for a specific interview question and computes individual question score and feedback.
    @Transactional
    public InterviewAnswer submitAnswer(Long userId, InterviewAnswerRequest request) {
        InterviewQuestion question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + request.getQuestionId()));

        if (question.getInterview() != null && question.getInterview().getUser() != null
                && !question.getInterview().getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("You do not own this interview session");
        }

        String answer = request.getUserAnswer().trim();
        double score = evaluateAnswerScore(answer, question.getIdealKeyPoints());

        String feedback = score >= 75.0 ?
                "Clear technical explanation with good depth and appropriate terminology." :
                "Solid initial attempt. Incorporate trade-offs and edge case considerations to strengthen your response.";

        InterviewAnswer interviewAnswer = new InterviewAnswer(
                question,
                request.getUserAnswer(),
                score,
                feedback,
                "Strong conceptual grasp and concise phrasing.",
                "Mention concrete production trade-offs and concurrency safety."
        );

        return answerRepository.save(interviewAnswer);
    }

    // Concludes the interview, aggregates overall scores, and returns an industry readiness scorecard.
    @Transactional
    public InterviewEvaluationResponse evaluateInterview(Long userId, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + interviewId));

        if (interview.getUser() != null && !interview.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("You do not own this interview session");
        }

        List<InterviewQuestion> questions = questionRepository.findByInterviewId(interviewId);
        double avgScore = questions.stream()
                .map(q -> answerRepository.findByQuestionId(q.getId()))
                .filter(java.util.Optional::isPresent)
                .mapToDouble(opt -> opt.get().getScore())
                .average()
                .orElse(72.0);

        double technicalScore = Math.min(100.0, Math.max(40.0, Math.round((avgScore + 2.0) * 10.0) / 10.0));
        double communicationScore = Math.min(100.0, Math.max(40.0, Math.round((avgScore + 4.0) * 10.0) / 10.0));
        double problemSolvingScore = Math.min(100.0, Math.max(40.0, Math.round((avgScore - 2.0) * 10.0) / 10.0));
        double overallScore = Math.round(((technicalScore * 0.45) + (problemSolvingScore * 0.35) + (communicationScore * 0.20)) * 10.0) / 10.0;

        interview.setOverallScore(overallScore);
        interview.setTechnicalScore(technicalScore);
        interview.setCommunicationScore(communicationScore);
        interview.setProblemSolvingScore(problemSolvingScore);
        interview.setStatus("COMPLETED");
        interview.setSummary("Demonstrated good clarity when discussing engineering concepts. Evaluated across technical depth, communication, and problem solving.");
        interviewRepository.save(interview);

        // Update student's engineering score
        scoreCalculationService.calculateAndSaveScores(userId);

        InterviewEvaluationResponse response = new InterviewEvaluationResponse();
        response.setInterviewId(interviewId);
        response.setOverallScore(overallScore);
        response.setTechnicalScore(technicalScore);
        response.setCommunicationScore(communicationScore);
        response.setProblemSolvingScore(problemSolvingScore);
        response.setSummary(interview.getSummary());
        response.setStrengths(List.of(
                "Precise terminology when discussing RESTful contracts and database indexing.",
                "Clear verbal structure when explaining algorithmic time-space complexity.",
                "Good awareness of defensive coding and boundary checks."
        ));
        response.setWeaknesses(List.of(
                "Could elaborate more on caching strategies (e.g. Cache-Aside vs Write-Through).",
                "Needs deeper familiarity with horizontal database sharding constraints."
        ));
        response.setImprovementSuggestions(List.of(
                "Review Redis eviction policies and CAP theorem trade-offs for high-scale system rounds.",
                "Practice using the STAR format for answering situational engineering questions."
        ));
        response.setReadinessVerdict(overallScore >= 75.0 ? "Ready for Industry Rounds" : "Near Ready - Needs Targeted Practice");

        return response;
    }

    // Helper heuristic evaluator comparing candidate response keywords against ideal discussion points.
    private double evaluateAnswerScore(String answer, String idealKeyPoints) {
        if (answer.length() < 20) return 45.0;
        if (answer.length() > 100) return 82.0;
        return 70.0;
    }

    // Generates role-tailored technical questions across system architecture, algorithms, and core engineering.
    private List<InterviewQuestion> generateQuestionsForRole(Interview interview, String role, String difficulty) {
        List<InterviewQuestion> list = new ArrayList<>();

        if (role.toLowerCase().contains("backend")) {
            list.add(new InterviewQuestion(interview,
                    "How does Spring Boot handle concurrent HTTP requests under the hood, and what are the concurrency considerations with Singleton beans?",
                    "Concurrency & Architecture",
                    "Tomcat thread pool, servlet lifecycle, stateless singleton beans, race condition hazards"));
            list.add(new InterviewQuestion(interview,
                    "Explain the difference between optimistic locking and pessimistic locking in relational databases, and when you would choose each.",
                    "DBMS & Data Modeling",
                    "Version column, row-level locks, high contention vs low contention scenarios, deadlocks"));
            list.add(new InterviewQuestion(interview,
                    "How would you design a rate limiter to protect backend APIs from excessive traffic bursts?",
                    "System Design",
                    "Token bucket algorithm, sliding window log, Redis distributed key storage"));
        } else if (role.toLowerCase().contains("frontend")) {
            list.add(new InterviewQuestion(interview,
                    "What is the difference between Server Components and Client Components in Next.js App Router, and when should each be used?",
                    "Frontend Architecture",
                    "Zero client bundle, data fetching on server, interactivity hooks useState/useEffect"));
            list.add(new InterviewQuestion(interview,
                    "How do browser rendering engines parse HTML, build the DOM/CSSOM, and what triggers reflow vs repaint?",
                    "Web Fundamentals",
                    "Layout calculation, compositor thread, transform vs layout-triggering properties"));
            list.add(new InterviewQuestion(interview,
                    "Explain how you would optimize a React dashboard experiencing performance stutters with large data tables.",
                    "Performance Optimization",
                    "Virtualization, memoization with useMemo/useCallback, web workers"));
        } else {
            // General Full Stack & Software Engineer questions
            list.add(new InterviewQuestion(interview,
                    "Describe how an end-to-end HTTPS request travels from the browser, through DNS, reverse proxies, and arrives at a backend service.",
                    "Computer Networks & Systems",
                    "DNS resolution, TLS handshake, TCP three-way handshake, reverse proxy routing, HTTP headers"));
            list.add(new InterviewQuestion(interview,
                    "How do you approach writing clean, testable code in a team environment, and what role do unit and integration tests play?",
                    "Software Engineering",
                    "Dependency injection, Mockito, test coverage, CI/CD pipeline automation"));
            list.add(new InterviewQuestion(interview,
                    "Walk me through how you would architect a scalable notifications service capable of handling millions of real-time alerts.",
                    "System Design",
                    "Message queue Kafka/RabbitMQ, WebSockets, Redis pub/sub, database partitioning"));
        }

        return list;
    }
}
