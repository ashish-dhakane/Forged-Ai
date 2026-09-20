package com.forgeai.entity;

import jakarta.persistence.*;

// Represents a specific engineering competency evaluated for a user.
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category; // Programming, Testing, Debugging, System Design, Security, Git

    private Double proficiency = 50.0; // 0 to 100

    private String level = "Intermediate"; // Beginner, Intermediate, Advanced

    public Skill() {}

    public Skill(User user, String name, String category, Double proficiency, String level) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.proficiency = proficiency;
        this.level = level;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getProficiency() { return proficiency; }
    public void setProficiency(Double proficiency) { this.proficiency = proficiency; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
