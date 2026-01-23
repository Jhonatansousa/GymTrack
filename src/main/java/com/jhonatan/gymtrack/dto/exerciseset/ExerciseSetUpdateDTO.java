package com.jhonatan.gymtrack.dto.exerciseset;


public record ExerciseSetUpdateDTO(
        String newName,
        Integer reps,
        Double weight
) {
}
