package com.forgeai.service;

import com.forgeai.dto.AssessmentResultDto;
import com.forgeai.dto.AssessmentSubmitRequest;
import com.forgeai.entity.*;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Coordinates technical assessment delivery, scores student submissions, and updates category competencies.
@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentResultRepository resultRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;

    public AssessmentService(AssessmentRepository assessmentRepository,
                             QuestionRepository questionRepository,
                             AssessmentResultRepository resultRepository,
                             UserRepository userRepository,
                             ScoreCalculationService scoreCalculationService) {
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Fetches all available skill assessments.
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    // Fetches a single assessment and its associated technical questions.
    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + id));
    }

    // Evaluates a user's submitted answers, records score percentage, and determines performance tier.
    @Transactional
    public AssessmentResultDto submitAssessment(Long userId, AssessmentSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + request.getAssessmentId()));

        List<Question> questions = questionRepository.findByAssessmentId(assessment.getId());
        int totalQuestions = questions.size();
        int correctCount = 0;

        Map<Long, Integer> userAnswers = request.getAnswers();
        for (Question q : questions) {
            Integer chosenOption = userAnswers.get(q.getId());
            if (chosenOption != null && chosenOption.equals(q.getCorrectOptionIndex())) {
                correctCount++;
            }
        }

        double scorePercentage = totalQuestions > 0 ? ((double) correctCount / totalQuestions) * 100.0 : 0.0;
        scorePercentage = Math.round(scorePercentage * 10.0) / 10.0;

        String tier;
        if (scorePercentage >= 80.0) {
            tier = "Strong";
        } else if (scorePercentage >= 60.0) {
            tier = "Average";
        } else {
            tier = "Needs Improvement";
        }

        AssessmentResult result = new AssessmentResult(
                user, assessment, scorePercentage, totalQuestions, correctCount, assessment.getCategory(), tier
        );
        AssessmentResult savedResult = resultRepository.save(result);

        // Awards XP based on assessment performance (up to 150 XP)
        int xpEarned = (int) (scorePercentage * 1.5);
        int newXp = (user.getXp() != null ? user.getXp() : 0) + xpEarned;
        user.setXp(newXp);
        user.setLevel(Math.max(1, (newXp / 500) + 1));
        userRepository.save(user);

        // Recalculates engineering score
        scoreCalculationService.calculateAndSaveScores(userId);

        AssessmentResultDto dto = new AssessmentResultDto();
        dto.setId(savedResult.getId());
        dto.setAssessmentId(assessment.getId());
        dto.setAssessmentTitle(assessment.getTitle());
        dto.setCategory(assessment.getCategory());
        dto.setScorePercentage(scorePercentage);
        dto.setCorrectCount(correctCount);
        dto.setTotalQuestions(totalQuestions);
        dto.setPerformanceTier(tier);
        dto.setXpAwarded(xpEarned);

        return dto;
    }

    // Fetches history of completed assessment results for a user.
    public List<AssessmentResult> getUserResults(Long userId) {
        return resultRepository.findByUserIdOrderByCompletedAtDesc(userId);
    }
}
