package com.jhonatan.gymtrack.factory;

import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.ErrorDTO;
import com.jhonatan.gymtrack.dto.enums.ResponseStatus;

import java.util.List;

public final class ApiResponseFactory {

    private ApiResponseFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> APIResponse<T> success(T result) {
        return APIResponse.<T>builder()
                .status(ResponseStatus.SUCCESS)
                .results(result)
                .build();
    }

    public static APIResponse<Void> success() {
        return APIResponse.<Void>builder()
                .status(ResponseStatus.SUCCESS)
                .build();
    }


    //errors
    public static <T> APIResponse<T> error(List<ErrorDTO> errors) {
        return APIResponse.<T>builder()
                .status(ResponseStatus.ERROR)
                .errors(errors)
                .build();
    }

    public static <T> APIResponse<T> error(String message) {
        return APIResponse.<T>builder()
                .status(ResponseStatus.ERROR)
                .errors(List.of(new ErrorDTO(null, message)))
                .build();
    }

    public static <T> APIResponse<T> error(String field, String message) {
        return APIResponse.<T>builder()
                .status(ResponseStatus.ERROR)
                .errors(List.of(new ErrorDTO(field, message)))
                .build();
    }
}
