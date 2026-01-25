package com.jhonatan.gymtrack.controller;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;
import com.jhonatan.gymtrack.service.IExerciseSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sets")
@RequiredArgsConstructor
public class ExerciseSetsController {


    private final IExerciseSetService service;

    @PostMapping
    public ResponseEntity<APIResponse<ExerciseSetResponseDTO>> createSet(@Valid @RequestBody ExerciseSetDTO dto) {

        ExerciseSetResponseDTO res = service.createNewSet(dto);

        APIResponse<ExerciseSetResponseDTO> apiResponse = APIResponse.<ExerciseSetResponseDTO>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{exerciseSetId}")
    public ResponseEntity<Void> updateSet(
            @PathVariable Long exerciseSetId,
            @Valid @RequestBody ExerciseSetUpdateDTO dto) {

        service.updateExerciseSet(exerciseSetId, dto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{exerciseSetId}")
    public ResponseEntity<Void> deleteSet(@PathVariable Long exerciseSetId) {
        service.deleteExerciseSet(exerciseSetId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
