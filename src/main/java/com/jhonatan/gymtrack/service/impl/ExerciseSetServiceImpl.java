package com.jhonatan.gymtrack.service.impl;


import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;
import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.ExerciseSet;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.ExerciseSetMapper;
import com.jhonatan.gymtrack.repository.ExerciseRepo;
import com.jhonatan.gymtrack.repository.ExerciseSetRepo;
import com.jhonatan.gymtrack.service.IExerciseSetService;
import com.jhonatan.gymtrack.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseSetServiceImpl implements IExerciseSetService {

    private final ExerciseSetRepo setRepo;
    private final ExerciseRepo exerciseRepo;
    private final UserContext userContext;
    private final ExerciseSetMapper mapper;

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public ExerciseSetResponseDTO createNewSet(ExerciseSetDTO dto) {
        User currentUser = userContext.getCurrentUser();

        Exercise exercise = exerciseRepo.findById(dto.exerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        if(!exercise.getWorkoutDivision().getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Exercise not found or Access Denied");
        }

        String finalName = dto.name();
        if (finalName == null || finalName.isEmpty()) {
            Integer totalSets = setRepo.countByExercise(exercise);
            finalName = String.valueOf(totalSets + 1);
        }

        Integer finalReps =  (dto.reps() != null) ? dto.reps() : 0;
        Double finalWeight = (dto.weight() != null) ? dto.weight() : 0.0;

        ExerciseSet exerciseSet = ExerciseSet.builder()
                .name(finalName)
                .reps(finalReps)
                .weight(finalWeight)
                .exercise(exercise)
                .build();

        return mapper.toDTO(setRepo.save(exerciseSet));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseSetResponseDTO> getSetsByExercise(Long exerciseId) {
        User currentUser = userContext.getCurrentUser();

        Exercise exercise = exerciseRepo.findByIdAndUser(exerciseId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise Not Found!"));

        List<ExerciseSet> sets = setRepo.findAllByExerciseIdOrderByIdAsc(exercise.getId());

        return sets.stream().map(mapper::toDTO).toList();
    }


    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void updateExerciseSet(Long id, ExerciseSetUpdateDTO dto) {
        User currentUser = userContext.getCurrentUser();

        ExerciseSet exerciseSet = setRepo.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Set not found"));


        if (dto.newName() != null) {
            exerciseSet.setName(dto.newName());
        }

        if (dto.reps() != null) {
            exerciseSet.setReps(dto.reps());
        }

        if (dto.weight() != null) {
            exerciseSet.setWeight(dto.weight());
        }
    }

    @Override
    @Transactional
    public void deleteExerciseSet(Long id) {
        User currentUser = userContext.getCurrentUser();
        ExerciseSet exerciseSet = setRepo.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Set Set not found"));

        setRepo.delete(exerciseSet);
    }

}
