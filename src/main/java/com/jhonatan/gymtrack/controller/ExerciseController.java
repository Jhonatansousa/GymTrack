package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;
import com.jhonatan.gymtrack.service.IExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercises")
public class ExerciseController {

    public final IExerciseService service;

    @PostMapping
    public ResponseEntity<APIResponse<ExerciseResponseDTO>> newExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {

        ExerciseResponseDTO res = service.createExercise(exerciseDTO);

        APIResponse<ExerciseResponseDTO> apiResponse = APIResponse.<ExerciseResponseDTO>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new  ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping("/{divisionId}")
    public ResponseEntity<APIResponse<List<ExerciseResponseDTO>>> getExercisesByDivision(@PathVariable Long divisionId) {

        List<ExerciseResponseDTO> res = service.getExercisesByDivision(divisionId);

        APIResponse<List<ExerciseResponseDTO>> apiResponse = APIResponse.<List<ExerciseResponseDTO>>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/{exerciseId}")
    public ResponseEntity<Void> updateExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateDTO dto) {


        service.updateExercise(exerciseId, dto);


        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long exerciseId) {
        service.deleteExercise(exerciseId);

        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
