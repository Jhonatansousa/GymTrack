package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;

public interface IExerciseService {
    ExerciseResponseDTO createExercise(ExerciseDTO exerciseDTO);

    void updateExercise(Long id, ExerciseUpdateDTO dto);
}
