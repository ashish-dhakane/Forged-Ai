package com.forgeai.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Represents an engineering skill assessment test containing technical questions.
@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category; // Programming, Testing, Debugging, System Design, Security, Git

    private String difficulty = "Intermediate"; // Beginner, Intermediate, Advanced

    private Integer durationMinutes = 20;

    private Integer totalQuestions = 5;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Assessment() {}

    public Assessment(String title, String category, String difficulty, Integer durationMinutes, String description) {
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}
