package com.jhonatan.gymtrack.dto.exerciseset;

public record ExerciseSetResponseDTO(
        Long exerciseId,
        Long exerciseSetId,
        String setName,
        Integer reps,
        Double weight
) {
}
