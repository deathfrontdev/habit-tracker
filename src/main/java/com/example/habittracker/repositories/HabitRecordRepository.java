package com.example.habittracker.repositories;

import com.example.habittracker.models.HabitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
    List<HabitRecord> findByHabitId(Long habitId);
    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);
}
