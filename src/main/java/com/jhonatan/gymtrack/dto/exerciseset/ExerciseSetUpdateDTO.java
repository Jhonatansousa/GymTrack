package com.jhonatan.gymtrack.dto.exerciseset;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExerciseSetUpdateDTO", description = "Request DTO for updating an existing exercise set")
public record ExerciseSetUpdateDTO(

        @Schema(description = "New name for the set", example = "Set B")
        String newName,

        @Schema(description = "Updated number of repetitions", example = "12")
        Integer reps,

        @Schema(description = "Updated weight for this set in kilograms", example = "70.8")
        Double weight
) {
}
