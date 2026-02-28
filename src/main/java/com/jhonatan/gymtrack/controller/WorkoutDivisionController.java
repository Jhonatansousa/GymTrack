package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.service.IWorkoutService;
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
@RequestMapping("/api/v1/divisions")
@Tag(name = "Workout Divisions", description = "Endpoints for managing user workout divisions (e.g., Push/Pull/Legs)")
public class WorkoutDivisionController {

    private final IWorkoutService workoutService;

    @Operation(summary = "Create a new division", description = "Creates a new Workout Division for authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Division created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Division name already exists")
    })
    @PostMapping
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> newDivision(@Valid @RequestBody WorkoutDivisionDTO dto) {

        WorkoutDivisionResponseDTO res = workoutService.createDivision(dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all divisions", description = "Rerieves all workout divisions belonging to the authenticated user")
    @ApiResponse(responseCode = "200", description = "List of divisions retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<WorkoutDivisionResponseDTO>>> getAllDivisions() {

        List<WorkoutDivisionResponseDTO> res = workoutService.getAllDivisions();
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a division",
            description = "Updates an existing workout division for the authenticated user (only the provided fields will be updated)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Division updated successfully"),
            @ApiResponse(responseCode = "400", description = "invalid input data"),
            @ApiResponse(responseCode = "404", description = "Division not found"),
            @ApiResponse(responseCode = "409", description = "Division name Already exists")
    })
    @PatchMapping("/{divisionId}")//clean URL -> PATCH /divisions/1
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> updateDivision(
            @PathVariable Long divisionId,
            @Valid @RequestBody DivisionUpdateDTO dto) {

        WorkoutDivisionResponseDTO res = workoutService.updateDivision(divisionId, dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }


    @Operation(
            summary = "Delete a division",
            description = "Deletes an existing workout belonging to the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Division deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Division not found")
    })
    @DeleteMapping("/{divisionId}")
    public ResponseEntity<APIResponse<Void>> deleteDivision(@PathVariable long divisionId) {

        workoutService.deleteDivision(divisionId);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }


}
