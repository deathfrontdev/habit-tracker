package com.example.habittracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean completed;
    @Column(name = "completion_rate")
    private double completionRate;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HabitRecord> records = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<HabitRecord> getRecords() {
        return records;
    }

    public void setRecords(List<HabitRecord> records) {
        this.records = records;
    }


    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public double getCompletionRate() {
        return completionRate;
    }
}
