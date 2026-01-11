package com.jhonatan.gymtrack.dto;

public record ExerciseResponseDTO(
        Long exerciseId,
        String exerciseName,
        Long workoutDivisionId
) { }
