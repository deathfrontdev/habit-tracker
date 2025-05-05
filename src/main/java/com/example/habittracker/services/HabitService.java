package com.example.habittracker.services;

import com.example.habittracker.models.Habit;
import com.example.habittracker.repositories.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {
    private HabitRepository habitRepo;
    @Autowired
    public HabitService(HabitRepository habitRepo){
        this.habitRepo = habitRepo;
    }

    public List<Habit> getAllHabits() {
        return habitRepo.findAll();
    }

    public Habit createHabit(Habit habit) {
        return habitRepo.save(habit);
    }
    public boolean existsById(Long id) {
        return habitRepo.existsById(id);
    }

    public Habit updateHabit(Long id, Habit updated) {
        Habit habit = habitRepo.findById(id).orElseThrow();
        habit.setName(updated.getName());
        habit.setDescription(updated.getDescription());
        habit.setCompleted(updated.isCompleted());
        return habitRepo.save(habit);
    }

    public void deleteHabit(Long id) {
        habitRepo.deleteById(id);
    }

    public Optional<Habit> getHabit(Long id) {
        return habitRepo.findById(id);
    }
}
