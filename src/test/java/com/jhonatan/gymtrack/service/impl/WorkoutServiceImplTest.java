package com.jhonatan.gymtrack.service.impl;


import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.DuplicatedContentException;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.WorkoutDivisionMapper;
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
public class WorkoutServiceImplTest {

    @Mock
    private WorkoutDivisionRepo repo;

    @Mock
    private UserContext userContext;

    @Mock
    private WorkoutDivisionMapper mapper;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    //create

    @Test
    @DisplayName("should create successfully workoutDivision when name doesn't exists previously")
    void shouldCreateWorkoutDivisionSuccessfully() {
        User user = User.builder().id(UUID.randomUUID()).build();

        WorkoutDivisionDTO dtoRequest = new WorkoutDivisionDTO("new division");
        WorkoutDivision newDivision = WorkoutDivision.builder().name("new division").build();
        WorkoutDivision savedDivision = WorkoutDivision.builder().id(1L).name("new division").user(user).build();
        WorkoutDivisionResponseDTO expectedResponse = new WorkoutDivisionResponseDTO(1L, "new division");


        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.existsByNameIgnoreCaseAndUser(dtoRequest.name(), user)).thenReturn(Boolean.FALSE);
        when(mapper.toEntity(dtoRequest)).thenReturn(newDivision);
        when(repo.save(newDivision)).thenReturn(savedDivision);
        when(mapper.toDTO(savedDivision)).thenReturn(expectedResponse);

        WorkoutDivisionResponseDTO result = workoutService.createDivision(dtoRequest);

        assertNotNull(result);
        assertEquals(result, expectedResponse);
        verify(repo, times(1)).existsByNameIgnoreCaseAndUser(dtoRequest.name(), user);
        verify(mapper, times(1)).toEntity(dtoRequest);
        verify(repo, times(1)).save(newDivision);
        verify(mapper, times(1)).toDTO(savedDivision);

    }

    @Test
    @DisplayName("should throw DuplicatedContentException when division name already exists")
    void shouldThrowDuplicatedContentException_WhenWorkoutDivisionNameAlreadyExists() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivisionDTO dtoRequest = new WorkoutDivisionDTO("division name duplicated");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.existsByNameIgnoreCaseAndUser(dtoRequest.name(), user)).thenReturn(true);

        assertThrows(DuplicatedContentException.class, () -> workoutService.createDivision(dtoRequest));
        verify(repo, times(1)).existsByNameIgnoreCaseAndUser(dtoRequest.name(), user);
        verify(mapper, never()).toEntity(any());
        verify(repo, never()).save(any());
        verify(mapper, never()).toDTO(any());

    }

    //get-list

    @Test
    @DisplayName("should return Exercise Division list")
    void shouldReturnExerciseDivisionList() {
        User user = User.builder().id(UUID.randomUUID()).build();


        List<WorkoutDivision> divisionsFromDb = List.of(
            WorkoutDivision.builder().id(1L).name("div1").build(),
            WorkoutDivision.builder().id(2L).name("div2").build()
        );

        WorkoutDivisionResponseDTO dtoRes1 = new WorkoutDivisionResponseDTO(1L, "div1");
        WorkoutDivisionResponseDTO dtoRes2 = new WorkoutDivisionResponseDTO(2L, "div2");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findAllByUser(user)).thenReturn(divisionsFromDb);
        when(mapper.toDTO(divisionsFromDb.get(0))).thenReturn(dtoRes1);
        when(mapper.toDTO(divisionsFromDb.get(1))).thenReturn(dtoRes2);

        List<WorkoutDivisionResponseDTO> result = workoutService.getAllDivisions();

        assertEquals(2, result.size());
        assertEquals("div1", result.get(0).name());
        assertEquals(dtoRes1, result.get(0));
        assertEquals("div2", result.get(1).name());
        assertEquals(dtoRes2, result.get(1));

    }


    @Test
    @DisplayName("Should return empty list when has no division yet")
    void shouldReturnEmptyList_WhenNoWorkoutDivisionYet() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findAllByUser(user)).thenReturn(Collections.emptyList());
        List<WorkoutDivisionResponseDTO> result = workoutService.getAllDivisions();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    //update

    @Test
    @DisplayName("Should update workout division successfully")
    void shouldUpdateWorkoutDivisionSuccessfuly() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision existingDivision = WorkoutDivision.builder().id(1L).name("division name").build();
        DivisionUpdateDTO dto = new DivisionUpdateDTO("updated division name");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findByIdAndUser(1L, user)).thenReturn(Optional.of(existingDivision));
        when(repo.existsByNameIgnoreCaseAndUser("updated division name", user)).thenReturn(Boolean.FALSE);

        workoutService.updateDivision(1L, dto);

        assertEquals("updated division name", existingDivision.getName());

    }

    @Test
    @DisplayName("SHould not hit database for validation when name is exactly the same")
    void shouldNotValidateDuplicity_WhenNameIsTheSame() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).name("equals division name").build();
        DivisionUpdateDTO dto = new DivisionUpdateDTO("equals division name");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findByIdAndUser(1L, user)).thenReturn(Optional.of(division));

        workoutService.updateDivision(1L, dto);

        verify(repo, never()).existsByNameIgnoreCaseAndUser("equals division name", user);
        assertEquals("equals division name", division.getName());
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException when updating invalid division")
    void shouldThrowResourceNotFoundException_WhenUpdatingInvalidDivision() {
        User user = User.builder().id(UUID.randomUUID()).build();
        DivisionUpdateDTO dto = new DivisionUpdateDTO("new name");

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findByIdAndUser(123L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workoutService.updateDivision(123L, dto));

        verify(repo, never()).existsByNameIgnoreCaseAndUser("new name", user);
    }

    //delete
    @Test
    @DisplayName("Should delete workout division successfully when data is valid")
    void shouldDeleteWorkoutDivision_WhenDataIsValid(){
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().id(1L).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findByIdAndUser(1L, user)).thenReturn(Optional.of(division));

        workoutService.deleteDivision(1L);

        verify(repo, times(1)).findByIdAndUser(1L, user);
        verify(repo, times(1)).delete(any());

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting invalid workoutdivision")
    void shouldThrowResourceNotFoundException_WhenDeletingInvalidWorkoutDivision(){
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(repo.findByIdAndUser(123L, user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> workoutService.deleteDivision(123L));
        verify(repo, never()).delete(any());
    }
}
