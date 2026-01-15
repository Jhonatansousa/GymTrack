package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.service.IExerciseSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sets")
@RequiredArgsConstructor
public class ExerciseSetsController {


    private final IExerciseSetService service;

    @PostMapping("/new")
    public ResponseEntity<APIResponse<ExerciseSetResponseDTO>> createSet(@Valid @RequestBody ExerciseSetDTO dto) {

        ExerciseSetResponseDTO res = service.createNewSet(dto);

        APIResponse<ExerciseSetResponseDTO> apiResponse = APIResponse.<ExerciseSetResponseDTO>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
