package com.jhonatan.gymtrack.dto.workoutdivision;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "WorkoutDivisionDTO", description = "Request DTO for creating a new workout division")
public record WorkoutDivisionDTO(
        @Schema(description = "Name of the workout division", example = "Push/Pull/Legs")
        String name
) {}