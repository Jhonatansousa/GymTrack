package com.jhonatan.gymtrack.dto.exercise;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ExerciseDTO", description = "Request DTO for creating a new exercise")


public record ExerciseDTO(
        @Schema(description = "ID of the workout division to which the exercise belongs", example = "1")
        long workoutDivisionId,

        @NotBlank
        @Schema(description = "Name of the exercise", example = "Bench Press")
        String name
) { }
