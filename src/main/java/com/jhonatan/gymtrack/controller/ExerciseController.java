package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.service.IExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercises")
@Tag(name = "Exercises", description = "Endpoint for managing user Exercisises (e.g., Bench Press, Deadlift, Squat")
public class ExerciseController {

    public final IExerciseService service;

    @Operation(summary = "Create a new Exercise", description = "Creates a new exercise for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercise create successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Exercise name already exists")
    })
    @PostMapping
    public ResponseEntity<APIResponse<ExerciseResponseDTO>> newExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {

        ExerciseResponseDTO res = service.createExercise(exerciseDTO);
        return new  ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get exercises by division",
            description = "Retrieves all exercises belonging to a specific workout division for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of exercises retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Division not found")
    })
    @GetMapping("/{divisionId}")
    public ResponseEntity<APIResponse<List<ExerciseResponseDTO>>> getExercisesByDivision(@PathVariable Long divisionId) {

        List<ExerciseResponseDTO> res = service.getExercisesByDivision(divisionId);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }



    @Operation(
            summary = "Update an exercise",
            description = "Updates an existing exercise for the authenticated user (Only the provided fields will be updated)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
            @ApiResponse(responseCode = "409", description = "Exercise name already exists")
    })
    @PatchMapping("/{exerciseId}")
    public ResponseEntity<APIResponse<Void>> updateExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateDTO dto) {

        service.updateExercise(exerciseId, dto);
        return new  ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }



    @Operation(
            summary = "Delete an exercise",
            description = "Deletes an existing exercise belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
    })
    @DeleteMapping("{exerciseId}")
    public ResponseEntity<APIResponse<Void>> deleteExercise(@PathVariable Long exerciseId) {

        service.deleteExercise(exerciseId);
        return new  ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }
}
