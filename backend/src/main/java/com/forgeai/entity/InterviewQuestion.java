package com.forgeai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// Represents an individual question presented during a technical interview session.
@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    @JsonIgnore
    private Interview interview;

    @Column(nullable = false, length = 2000)
    private String questionText;

    private String category;

    @Column(length = 2000)
    private String idealKeyPoints;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private InterviewAnswer answer;

    public InterviewQuestion() {}

    public InterviewQuestion(Interview interview, String questionText, String category, String idealKeyPoints) {
        this.interview = interview;
        this.questionText = questionText;
        this.category = category;
        this.idealKeyPoints = idealKeyPoints;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Interview getInterview() { return interview; }
    public void setInterview(Interview interview) { this.interview = interview; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getIdealKeyPoints() { return idealKeyPoints; }
    public void setIdealKeyPoints(String idealKeyPoints) { this.idealKeyPoints = idealKeyPoints; }

    public InterviewAnswer getAnswer() { return answer; }
    public void setAnswer(InterviewAnswer answer) { this.answer = answer; }
}
