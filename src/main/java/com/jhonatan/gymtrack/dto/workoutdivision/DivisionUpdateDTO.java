package com.jhonatan.gymtrack.dto.workoutdivision;

import jakarta.validation.constraints.NotBlank;

public record DivisionUpdateDTO(
        @NotBlank(message = "Name cannot be empty")
        String newName
) {
}
