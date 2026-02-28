package com.jhonatan.gymtrack.dto.exercise;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ExerciseUpdateDTO", description = "Request DTO for updating an exercise name")
public record ExerciseUpdateDTO(
        @Schema(description = "New name for the exercise", example = "Incline Bench Press")
        @NotBlank(message = "new name cannot be empty")
        String newExerciseName
) {
}
