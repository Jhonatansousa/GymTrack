package com.jhonatan.gymtrack.dto.exerciseset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ExerciseSetDTO(
        long exerciseId,
        @NotBlank
        String name,
        @PositiveOrZero
        Integer reps,
        @PositiveOrZero
        Double weight

) { }
