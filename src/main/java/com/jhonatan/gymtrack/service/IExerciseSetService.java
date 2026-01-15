package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;

public interface IExerciseSetService {
    ExerciseSetResponseDTO createNewSet(ExerciseSetDTO exerciseSetDTO);

}
