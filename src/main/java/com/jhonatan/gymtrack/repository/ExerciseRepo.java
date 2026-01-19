package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExerciseRepo extends JpaRepository<Exercise, Long> {

    @Query("SELECT e FROM Exercise e WHERE e.id = :id AND e.workoutDivision.user = :user")
    Optional<Exercise> findByIdAndUser(Long id, User user);

    // Para validar duplicidade de nome DENTRO da mesma divisão na hora do update
    boolean existsByNameIgnoreCaseAndWorkoutDivision(String name, WorkoutDivision workoutDivision);

}
