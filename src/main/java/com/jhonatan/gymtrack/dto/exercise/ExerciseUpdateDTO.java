package com.jhonatan.gymtrack.dto.exercise;

import jakarta.validation.constraints.NotBlank;

public record ExerciseUpdateDTO(
        @NotBlank(message = "new name cannot be empty")
        String newExerciseName
) {
}
