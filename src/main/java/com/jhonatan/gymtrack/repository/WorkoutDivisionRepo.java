package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.WorkoutDivision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutDivisionRepo extends JpaRepository<WorkoutDivision, Long> {
}
