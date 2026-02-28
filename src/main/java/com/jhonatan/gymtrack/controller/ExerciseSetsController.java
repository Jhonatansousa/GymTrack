package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.service.IExerciseSetService;
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
@RequestMapping("/api/v1/sets")
@RequiredArgsConstructor
@Tag(name = "Exercise Sets", description = "Endpoints for managing exercise sets (e.g., weight: 50 (kg or lb) & reps: 10) for the authenticated user")
public class ExerciseSetsController {


    private final IExerciseSetService service;

    @Operation(
            summary = "Create a new exercise set",
            description = "Creates a new set for a specific exercise belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercise set created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Exercise Set not found")
    })
    @PostMapping
    public ResponseEntity<APIResponse<ExerciseSetResponseDTO>> createSet(@Valid @RequestBody ExerciseSetDTO dto) {

        ExerciseSetResponseDTO res = service.createNewSet(dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get sets by exercise",
            description = "Retrieves all sets associated with a specific exercise for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise Set not found")
    })
    @GetMapping("{exerciseId}")
    public ResponseEntity<APIResponse<List<ExerciseSetResponseDTO>>> getSetsByExercise(@PathVariable Long exerciseId) {

        List<ExerciseSetResponseDTO> res = service.getSetsByExercise(exerciseId);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }



    @Operation(
            summary = "Update an exercise set",
            description = "Updates an existing exercise set for the authenticated user (only the provided fields will be updated.)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise set updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Exercise set not found")
    })
    @PatchMapping("/{exerciseSetId}")
    public ResponseEntity<APIResponse<Void>> updateSet(
            @PathVariable Long exerciseSetId,
            @Valid @RequestBody ExerciseSetUpdateDTO dto) {

        service.updateExerciseSet(exerciseSetId, dto);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }



    @Operation(
            summary = "Delete an exercise set",
            description = "Deletes an existing exercise set belonging to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise set deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise set not found")
    })
    @DeleteMapping("/{exerciseSetId}")
    public ResponseEntity<APIResponse<Void>> deleteSet(@PathVariable Long exerciseSetId) {

        service.deleteExerciseSet(exerciseSetId);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }
}
