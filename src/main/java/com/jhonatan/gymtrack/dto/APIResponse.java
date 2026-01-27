package com.jhonatan.gymtrack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jhonatan.gymtrack.dto.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class APIResponse<T> {
    private ResponseStatus status;
    private List<ErrorDTO> errors;
    private T results;
}
