package com.jhonatan.gymtrack.dto;

public record ExerciseResponseDTO(
        Long id,
        String name,
        Long workoutDivisionId
) { }
