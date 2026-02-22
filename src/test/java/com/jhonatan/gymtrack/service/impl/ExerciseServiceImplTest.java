package com.jhonatan.gymtrack.service.impl;

import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;
import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.ExerciseMapper;
import com.jhonatan.gymtrack.repository.ExerciseRepo;
import com.jhonatan.gymtrack.repository.WorkoutDivisionRepo;
import com.jhonatan.gymtrack.service.UserContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepo exerciseRepo;

    @Mock
    private WorkoutDivisionRepo divisionRepo;

    @Mock
    private UserContext userContext;

    @Mock
    private ExerciseMapper mapper;

    @InjectMocks
    private ExerciseServiceImpl service;

    //create
    @Test
    @DisplayName("should create a new Exercise when data is valid")
    void shouldCreateNewExercise_WhenDataIsValid() {
        //arrange
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().user(user).id(1L).name("Chest").build();

        ExerciseDTO dto = new ExerciseDTO(1L, "Fly machine");
        Exercise exerciseToSave = Exercise.builder().name("Fly machine").build();
        Exercise savedExercise = Exercise.builder().id(10L).name("Fly machine").workoutDivision(division).build();
        ExerciseResponseDTO expectedResponse = new ExerciseResponseDTO(10L, "Fly machine", 1L);


        when(userContext.getCurrentUser()).thenReturn(user);
        when(divisionRepo.findByIdAndUser(1L, user)).thenReturn(Optional.of(division));
        when(mapper.toEntity(dto)).thenReturn(exerciseToSave);
        when(exerciseRepo.save(exerciseToSave)).thenReturn(savedExercise);
        when(mapper.toDTO(savedExercise)).thenReturn(expectedResponse);


        //actt
        ExerciseResponseDTO result = service.createExercise(dto);

        //assert
        assertNotNull(result);
        assertEquals("Fly machine", result.exerciseName());
        assertEquals(1L, result.workoutDivisionId());
        assertEquals(division, exerciseToSave.getWorkoutDivision());
        verify(exerciseRepo, times(1)).save(exerciseToSave);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when workout division is invalid")
    void shouldThrowResourceNotFoundException_WhenWorkoutDivisionIsInvalid() {
        User user = User.builder().id(UUID.randomUUID()).build();
        ExerciseDTO dto = new ExerciseDTO(99L, "exercise name test");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(divisionRepo.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createExercise(dto));
        verify(exerciseRepo, never()).save(any());
    }

    // GET
    @Test
    @DisplayName("Should Return a list of exercises when division exists")
    void shouldReturnExerciseList_WhenDivisionExists() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();

        List<Exercise> exerciseFromDb = List.of(
                Exercise.builder().id(10L).name("exercise 1").build(),
                Exercise.builder().id(11L).name("exercise 2").build()
        );

        ExerciseResponseDTO dtoRes1 = new ExerciseResponseDTO(10L, "exercise 1", 1L);
        ExerciseResponseDTO dtoRes2 = new ExerciseResponseDTO(11L, "exercise 2", 1L);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(divisionRepo.findByIdAndUser(1L, user)).thenReturn(Optional.of(division));
        when(exerciseRepo.findAllByWorkoutDivision(division)).thenReturn(exerciseFromDb);
        when(mapper.toDTO(exerciseFromDb.get(0))).thenReturn(dtoRes1);
        when(mapper.toDTO(exerciseFromDb.get(1))).thenReturn(dtoRes2);

        List<ExerciseResponseDTO> result = service.getExercisesByDivision(1L);

        assertEquals(2, result.size());
        assertEquals("exercise 1", result.get(0).exerciseName());
        assertEquals(dtoRes1, result.get(0));
        assertEquals("exercise 2", result.get(1).exerciseName());
        assertEquals(dtoRes2, result.get(1));

    }

    @Test
    @DisplayName("Should return empty list when division has no exercises")
    void shoudReturnEmptyList_WhenDivisionHasNoExercises() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(divisionRepo.findByIdAndUser(1L, user)).thenReturn(Optional.of(division));
        when(exerciseRepo.findAllByWorkoutDivision(division)).thenReturn(Collections.emptyList());

        List<ExerciseResponseDTO> result = service.getExercisesByDivision(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when division is invalid")
    void shouldThrowResourceNotFoundException_WhenDivisionIsInvalid() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(divisionRepo.findByIdAndUser(123L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getExercisesByDivision(123L));
        verify(exerciseRepo, never()).findAllByWorkoutDivision(any());
    }

    /// update
    @Test
    @DisplayName("Should update exercise name successfully")
    void shouldUpdateExerciseNameSuccessfully() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();
        Exercise existingExercise = Exercise.builder().id(10L).name("exercise 1").workoutDivision(division).build();

        ExerciseUpdateDTO updateDTO = new ExerciseUpdateDTO("updated exercise 1");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(10L, user)).thenReturn(Optional.of(existingExercise));

        when(exerciseRepo.existsByNameIgnoreCaseAndWorkoutDivision("updated exercise 1", existingExercise.getWorkoutDivision())).thenReturn(false);
        //act
        service.updateExercise(10L, updateDTO);


        assertEquals("updated exercise 1", existingExercise.getName());

    }


    @Test
    @DisplayName("Should not hit database for validation when name is exactly the same")
    void shouldNotValidateDuplicity_WhenNameIsTheSame(){
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();
        Exercise existingExercise = Exercise.builder().id(10L).name("equals exercise name").workoutDivision(division).build();
        ExerciseUpdateDTO updateDTO = new ExerciseUpdateDTO("equals exercise name");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(10L, user)).thenReturn(Optional.of(existingExercise));

        service.updateExercise(10L, updateDTO);

        verify(exerciseRepo, never()).existsByNameIgnoreCaseAndWorkoutDivision(any(), any());
        assertEquals("equals exercise name", existingExercise.getName());

    }

    @Test
    @DisplayName("SHould throw ResourceNotFoundException when updating invalid exercise")
    void shouldThrowResourceNotFoundException_WhenUpdatingInvalidExercise() {
        User user = User.builder().id(UUID.randomUUID()).build();
        ExerciseUpdateDTO updateDTO = new ExerciseUpdateDTO("new name");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(123L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateExercise(123L, updateDTO));
    }


    //DELETE

    @Test
    @DisplayName("SHould delete exercise successfully")
    void shouldDeleteExerciseSuccessfully_WhenDataIsValid() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();
        Exercise exerciseToDelete = Exercise.builder().id(10L).workoutDivision(division).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(10L, user)).thenReturn(Optional.of(exerciseToDelete));
        service.deleteExercise(10L);

        verify(exerciseRepo, times(1)).findByIdAndUser(10L, user);
        verify(exerciseRepo, times(1)).delete(exerciseToDelete);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting invalid exercise")
    void shouldThrowResourceNotFoundException_WhenDeletingInvalidExercise() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(10L, user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteExercise(10L));
        verify(exerciseRepo, never()).delete(any());

    }

}
