package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.WorkoutDivisionResponseDTO;

public interface IWorkoutService {
    WorkoutDivisionResponseDTO createDivision(WorkoutDivisionDTO workoutDivisionDTO);
}

