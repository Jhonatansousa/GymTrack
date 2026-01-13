package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.ExerciseSetResponseDTO;

public interface IExerciseSetService {
    ExerciseSetResponseDTO createNewSet(ExerciseSetDTO exerciseSetDTO);

}
