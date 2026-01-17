package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exercise.ExerciseDTO;
import com.jhonatan.gymtrack.dto.exercise.ExerciseResponseDTO;
import com.jhonatan.gymtrack.service.IExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
