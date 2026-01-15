package com.jhonatan.gymtrack.dto.exercise;

public record ExerciseResponseDTO(
        Long exerciseId,
        String exerciseName,
        Long workoutDivisionId
) { }
