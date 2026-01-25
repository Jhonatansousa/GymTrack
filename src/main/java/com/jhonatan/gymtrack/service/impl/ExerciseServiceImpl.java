package com.jhonatan.gymtrack.service.impl;

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
import com.jhonatan.gymtrack.repository.WorkoutDivisionRepo;
import com.jhonatan.gymtrack.service.IExerciseService;
import com.jhonatan.gymtrack.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional(readOnly = true)
    public List<ExerciseResponseDTO> getExercisesByDivision(Long divisionId) {
        User user = userContext.getCurrentUser();

        WorkoutDivision division = divisionRepo.findByIdAndUser(divisionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Workout Division Not Found!"));

        List<Exercise> exercises = repo.findAllByWorkoutDivision(division);

        return exercises.stream().map(mapper::toDTO).toList();

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

    @Override
    @Transactional
    public void deleteExercise(Long exerciseId) {
        User user = userContext.getCurrentUser();

        Exercise exercise = repo.findByIdAndUser(exerciseId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise Not Found!"));

        repo.delete(exercise);
    }
}
