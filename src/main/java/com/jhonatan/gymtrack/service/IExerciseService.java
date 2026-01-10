package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.ExerciseDTO;
import com.jhonatan.gymtrack.dto.ExerciseResponseDTO;

public interface IExerciseService {
    ExerciseResponseDTO createExercise(ExerciseDTO exerciseDTO);
}
