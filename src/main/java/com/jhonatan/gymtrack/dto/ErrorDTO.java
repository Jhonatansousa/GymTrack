package com.jhonatan.gymtrack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDTO(
        String field, String errorMessage
) { }