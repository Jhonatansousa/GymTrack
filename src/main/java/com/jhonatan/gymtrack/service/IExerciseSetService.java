package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;

import java.util.List;

public interface IExerciseSetService {
    ExerciseSetResponseDTO createNewSet(ExerciseSetDTO exerciseSetDTO);

    void updateExerciseSet(Long id, ExerciseSetUpdateDTO exerciseSetUpdateDTO);

    void deleteExerciseSet(Long exerciseSetId);

    List<ExerciseSetResponseDTO> getSetsByExercise(Long exerciseId);
}
