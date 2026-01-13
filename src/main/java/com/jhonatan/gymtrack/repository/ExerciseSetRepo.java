package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseSetRepo extends JpaRepository<ExerciseSet, Long> {
    Integer countByExercise(Exercise exercise);
}
