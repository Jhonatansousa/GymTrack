package com.jhonatan.gymtrack.dto.workoutdivision;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "DivisionUpdateDTO", description = "Request DTO for updating a workout division name")
public record DivisionUpdateDTO(
        @NotBlank(message = "Name cannot be empty")
        @Schema(description = "New name for the workout division", example = "Push/Pull/Legs")
        String newName
) {
}
