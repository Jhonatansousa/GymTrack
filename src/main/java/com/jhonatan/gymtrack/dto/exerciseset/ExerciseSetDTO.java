package com.jhonatan.gymtrack.dto.exerciseset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "ExerciseSetDTO", description = "Request DTO for creating a new exercise set")
public record ExerciseSetDTO(

        @Schema(description = "ID of the exercise to which the set belongs", example = "5")
        long exerciseId,

        @NotBlank
        @Schema(description = "Name of the set", example = "Warmup set")
        String name,

        @PositiveOrZero
        @Schema(description = "Number of repetitions for this set", example = "10")
        Integer reps,
        @PositiveOrZero
        @Schema(description = "Weight used for this set in kilograms or pounds (only number)", example = "60.5")
        Double weight

) { }
