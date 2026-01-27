package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.service.IExerciseSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sets")
@RequiredArgsConstructor
public class ExerciseSetsController {


    private final IExerciseSetService service;

    @PostMapping
    public ResponseEntity<APIResponse<ExerciseSetResponseDTO>> createSet(@Valid @RequestBody ExerciseSetDTO dto) {

        ExerciseSetResponseDTO res = service.createNewSet(dto);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.CREATED);
    }


    @GetMapping("{exerciseId}")
    public ResponseEntity<APIResponse<List<ExerciseSetResponseDTO>>> getSetsByExercise(@PathVariable Long exerciseId) {

        List<ExerciseSetResponseDTO> res = service.getSetsByExercise(exerciseId);
        return new ResponseEntity<>(ApiResponseFactory.success(res), HttpStatus.OK);
    }


    @PatchMapping("/{exerciseSetId}")
    public ResponseEntity<APIResponse<Void>> updateSet(
            @PathVariable Long exerciseSetId,
            @Valid @RequestBody ExerciseSetUpdateDTO dto) {

        service.updateExerciseSet(exerciseSetId, dto);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }


    @DeleteMapping("/{exerciseSetId}")
    public ResponseEntity<APIResponse<Void>> deleteSet(@PathVariable Long exerciseSetId) {

        service.deleteExerciseSet(exerciseSetId);
        return new ResponseEntity<>(ApiResponseFactory.success(), HttpStatus.OK);
    }
}
