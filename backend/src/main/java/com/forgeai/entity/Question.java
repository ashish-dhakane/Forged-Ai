package com.forgeai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// Represents a multiple-choice or conceptual question belonging to an assessment.
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    @JsonIgnore
    private Assessment assessment;

    @Column(nullable = false, length = 2000)
    private String questionText;

    private String category;

    private String difficulty;

    // Stored as a delimited string or JSON string, e.g. ["A","B","C","D"]
    @Column(nullable = false, length = 2000)
    private String optionsJson;

    @Column(nullable = false)
    private Integer correctOptionIndex;

    @Column(length = 2000)
    private String explanation;

    public Question() {}

    public Question(Assessment assessment, String questionText, String category, String difficulty, String optionsJson, Integer correctOptionIndex, String explanation) {
        this.assessment = assessment;
        this.questionText = questionText;
        this.category = category;
        this.difficulty = difficulty;
        this.optionsJson = optionsJson;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getOptionsJson() { return optionsJson; }
    public void setOptionsJson(String optionsJson) { this.optionsJson = optionsJson; }

    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(Integer correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
