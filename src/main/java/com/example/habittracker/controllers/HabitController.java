package com.example.habittracker.controllers;

import com.example.habittracker.models.Habit;
import com.example.habittracker.models.HabitRecord;
import com.example.habittracker.services.HabitRecordService;
import com.example.habittracker.services.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;
    private final HabitRecordService recordService;

    @Autowired
    public HabitController(HabitService habitService, HabitRecordService recordService) {
        this.habitService = habitService;
        this.recordService = recordService;
    }

    @GetMapping
    public String getAllHabits(Model model) {
        List<Habit> habits = habitService.getAllHabits();
        model.addAttribute("habits", habits);
        model.addAttribute("habit", new Habit());
        model.addAttribute("today", LocalDate.now());

        Map<Long, Boolean> doneTodayMap = new HashMap<>();
        for (Habit habit : habits) {
            boolean doneToday = habit.getRecords().stream()
                    .anyMatch(r -> r.getDate().equals(LocalDate.now()));
            doneTodayMap.put(habit.getId(), doneToday);
        }

        model.addAttribute("doneToday", doneTodayMap);
        return "habit-list";
    }

    @PostMapping
    public String createHabit(@ModelAttribute Habit habit) {
        habitService.createHabit(habit);
        return "redirect:/habits";
    }

    @PostMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return "redirect:/habits";
    }

    @GetMapping("/edit/{id}")
    public String editHabit(@PathVariable Long id, Model model) {
        Habit habit = habitService.getHabit(id).orElseThrow();
        model.addAttribute("habit", habit);
        return "habit-edit";
    }

    @PostMapping("/update/{id}")
    public String updateHabit(@PathVariable Long id, @ModelAttribute Habit updated) {
        habitService.updateHabit(id, updated);
        return "redirect:/habits";
    }

    @PostMapping("/{id}/records")
    public String addRecord(@PathVariable Long id, @RequestParam("date") String date, Model model) {
        LocalDate parsedDate = LocalDate.parse(date);
        try {
            recordService.addRecord(id, parsedDate);
            Habit habit = habitService.getHabit(id).orElseThrow();
            habit.setCompleted(true);
            habitService.updateHabit(id, habit);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Привычка уже отмечена сегодня.");
        }
        return "redirect:/habits";
    }


    @PostMapping("/records/delete/{recordId}")
    public String deleteRecord(@PathVariable Long recordId, @RequestParam("habitId") Long habitId) {
        recordService.deleteRecord(recordId);
        return "redirect:/habits/" + habitId + "/records";
    }

    @GetMapping("/{id}/records")
    public String viewHabitRecords(@PathVariable Long id, Model model) {
        Habit habit = habitService.getHabit(id).orElseThrow();
        List<HabitRecord> records = recordService.getRecordsForHabit(id)
                .stream()
                .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                .toList();
        double progressPercentage = calculateProgress(habit);
        model.addAttribute("habit", habit);
        model.addAttribute("records", records);
        model.addAttribute("progressPercentage", progressPercentage);
        return "habit-records";
    }
    public double calculateProgress(Habit habit) {
        long completed = habit.getRecords().stream().filter(r -> habit.isCompleted()).count();
        return (completed * 100.0) / habit.getRecords().size();
    }

    @GetMapping("/history")
    public String getAllHabitsHistory(Model model) {
        List<Habit> habits = habitService.getAllHabits();
        for (Habit habit : habits) {
            double completionRate = calculateCompletionRate(habit);
            habit.setCompletionRate(completionRate);
        }
        model.addAttribute("habits", habits);
        return "habit-history";
    }

    public double calculateCompletionRate(Habit habit) {
        long completedCount = habit.getRecords().stream()
                .filter(record -> record.getHabit().isCompleted())
                .count();
        long totalDays = 66;
        return totalDays == 0 ? 0 : (completedCount * 100.0) / totalDays;
    }
}
