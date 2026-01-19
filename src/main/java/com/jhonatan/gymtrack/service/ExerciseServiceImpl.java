package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;
import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.DuplicatedContentException;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.ExerciseMapper;
import com.jhonatan.gymtrack.repository.ExerciseRepo;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.repository.WorkoutDivisionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {

    private final ExerciseRepo repo;
    private final WorkoutDivisionRepo divisionRepo;
    private final ExerciseMapper mapper;
    private final UserContext userContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExerciseResponseDTO createExercise(ExerciseDTO exerciseDTO) {

        User user = userContext.getCurrentUser();

        WorkoutDivision division = divisionRepo.findByIdAndUser(exerciseDTO.workoutDivisionId(), user)
                .orElseThrow( () -> new ResourceNotFoundException("Workout Division Not Found or Access Denied!"));

        Exercise exercise = mapper.toEntity(exerciseDTO);
        exercise.setWorkoutDivision(division);
        Exercise savedExercise = repo.save(exercise);
        return mapper.toDTO(savedExercise);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExercise(Long exerciseId, ExerciseUpdateDTO dto) {
        User user = userContext.getCurrentUser();

        Exercise exercise = repo.findByIdAndUser(exerciseId, user)
                .orElseThrow( () -> new ResourceNotFoundException("Exercise Not Found!"));



        if (!exercise.getName().equalsIgnoreCase(dto.newExerciseName()) &&
        repo.existsByNameIgnoreCaseAndWorkoutDivision(dto.newExerciseName(), exercise.getWorkoutDivision())) {
            throw new DuplicatedContentException("Exercise Name already exists in this division");
        }
        exercise.setName(dto.newExerciseName());
    }
}
