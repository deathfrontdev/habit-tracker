package com.example.habittracker.repositories;
import com.example.habittracker.models.Habit;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface HabitRepository extends JpaRepository<Habit, Long> {

}


