package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.service.IWorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/divisions")
public class WorkoutDivisionController {

    private final IWorkoutService workoutService;

    @PostMapping
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> newDivision(@Valid @RequestBody WorkoutDivisionDTO dto) {

        WorkoutDivisionResponseDTO res = workoutService.createDivision(dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<APIResponse<List<WorkoutDivisionResponseDTO>>> getAllDivisions() {

        List<WorkoutDivisionResponseDTO> res = workoutService.getAllDivisions();
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }

    @PatchMapping("/{divisionId}")//clean URL -> PATCH /divisions/1
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> updateDivision(
            @PathVariable Long divisionId,
            @Valid @RequestBody DivisionUpdateDTO dto) {

        WorkoutDivisionResponseDTO res = workoutService.updateDivision(divisionId, dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }


    @DeleteMapping("/{divisionId}")
    public ResponseEntity<APIResponse<Void>> deleteDivision(@PathVariable long divisionId) {

        workoutService.deleteDivision(divisionId);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }


}
