package com.example.habittracker.services;

import com.example.habittracker.models.Habit;
import com.example.habittracker.models.HabitRecord;
import com.example.habittracker.repositories.HabitRecordRepository;
import com.example.habittracker.repositories.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitRecordService {
    private HabitRecordRepository recordRepo;
    private HabitRepository habitRepo;

    @Autowired
    public HabitRecordService(HabitRepository habitRepo,HabitRecordRepository recordRepo){
        this.habitRepo = habitRepo;
        this.recordRepo = recordRepo;
    }

    public HabitRecord addRecord(Long habitId, LocalDate date) {
        if (recordRepo.existsByHabitIdAndDate(habitId, date)) {
            throw new IllegalArgumentException("Запись уже существует");
        }

        Habit habit = habitRepo.findById(habitId).orElseThrow();
        HabitRecord record = new HabitRecord();
        record.setDate(date);
        record.setHabit(habit);
        return recordRepo.save(record);
    }

    public void deleteRecord(Long recordId) {
        recordRepo.deleteById(recordId);
    }

    public List<HabitRecord> getRecordsForHabit(Long habitId) {
        return recordRepo.findByHabitId(habitId);
    }
}
