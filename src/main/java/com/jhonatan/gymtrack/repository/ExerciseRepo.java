package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepo extends JpaRepository<Exercise, Long> {
}
