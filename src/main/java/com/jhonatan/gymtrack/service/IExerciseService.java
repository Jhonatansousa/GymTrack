package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;

import java.util.List;

public interface IExerciseService {
    ExerciseResponseDTO createExercise(ExerciseDTO exerciseDTO);

    void updateExercise(Long id, ExerciseUpdateDTO dto);

    void deleteExercise(Long exerciseId);

    List<ExerciseResponseDTO> getExercisesByDivision(Long divisionId);
}
