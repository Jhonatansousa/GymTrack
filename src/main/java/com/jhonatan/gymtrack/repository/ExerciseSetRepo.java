package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.ExerciseSet;
import com.jhonatan.gymtrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExerciseSetRepo extends JpaRepository<ExerciseSet, Long> {
    Integer countByExercise(Exercise exercise);

    // Busca pelos 3 níveis
    /**Só retorne um ExerciseSet do ID especificado se ele pertencer à um Exercise
     * que pertence a uma WorkoutDivision
     * que pertence ao usuário específico passado no param -> (User user)*/
    @Query("SELECT s FROM ExerciseSet s WHERE s.id = :id AND s.exercise.workoutDivision.user = :user")
    Optional<ExerciseSet> findByIdAndUser(Long id, User user);
}
