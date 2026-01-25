package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;

public interface IExerciseSetService {
    ExerciseSetResponseDTO createNewSet(ExerciseSetDTO exerciseSetDTO);

    void updateExerciseSet(Long id, ExerciseSetUpdateDTO exerciseSetUpdateDTO);

    void deleteExerciseSet(Long exerciseSetId);
}
