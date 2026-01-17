package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
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

    @PatchMapping("/{divisionId}")//clean URL -> PATCH /divisions/1
    public ResponseEntity<APIResponse<WorkoutDivisionResponseDTO>> updateDivision(
            @PathVariable Long divisionId,
            @Valid @RequestBody DivisionUpdateDTO dto) {

        //nesse caso eu devo passar o id no path e o dto (novo nome) no body
        WorkoutDivisionResponseDTO res = workoutService.updateDivision(divisionId, dto);

        APIResponse<WorkoutDivisionResponseDTO> apiResponse = APIResponse.<WorkoutDivisionResponseDTO>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{divisionId}")
    public ResponseEntity<APIResponse<Void>> deleteDivision(@PathVariable long divisionId) {
        workoutService.deleteDivision(divisionId);

        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .status("SUCCESS")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
