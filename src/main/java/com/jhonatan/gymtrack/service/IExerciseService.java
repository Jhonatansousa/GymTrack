package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;

public interface IExerciseService {
    ExerciseResponseDTO createExercise(ExerciseDTO exerciseDTO);
}
