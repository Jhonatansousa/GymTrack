package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.service.IWorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/division")
public class WorkoutDivisionController {


    public final IWorkoutService workoutService;


    @PostMapping("/new")
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> newDivision(@Valid @RequestBody WorkoutDivisionDTO dto) {

        WorkoutDivisionResponseDTO res = workoutService.createDivision(dto);

        APIResponse<WorkoutDivisionResponseDTO> apiResponse = APIResponse.<WorkoutDivisionResponseDTO>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<APIResponse<List<WorkoutDivisionResponseDTO>>> getAllDivisions() {
        List<WorkoutDivisionResponseDTO> res = workoutService.getAllDivisions();

        APIResponse<List<WorkoutDivisionResponseDTO>> apiResponse = APIResponse.<List<WorkoutDivisionResponseDTO>>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
