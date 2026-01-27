package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseUpdateDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
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
        return new  ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }


    @GetMapping("/{divisionId}")
    public ResponseEntity<APIResponse<List<ExerciseResponseDTO>>> getExercisesByDivision(@PathVariable Long divisionId) {

        List<ExerciseResponseDTO> res = service.getExercisesByDivision(divisionId);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }


    @PatchMapping("/{exerciseId}")
    public ResponseEntity<APIResponse<Void>> updateExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody ExerciseUpdateDTO dto) {

        service.updateExercise(exerciseId, dto);
        return new  ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }


    @DeleteMapping("{exerciseId}")
    public ResponseEntity<APIResponse<Void>> deleteExercise(@PathVariable Long exerciseId) {

        service.deleteExercise(exerciseId);
        return new  ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }
}
