package com.homegravity.Odi.global.response.success;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "Api 데이터 모델")
public class ApiResponse<T> {
    @Schema(description = "Http 상태 코드")
    private final int status;

    @Schema(description = "응답 메세지")
    private final String message;

    @Schema(description = "응답 데이터")
    private final T data;

    @Builder
    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(SuccessCode resultCode, T data) {
        return ApiResponse.<T>builder()
                .status(resultCode.getStatus())
                .message(resultCode.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse<Void> of(SuccessCode resultCode) {
        return ApiResponse.<Void>builder()
                .status(resultCode.getStatus())
                .message(resultCode.getMessage())
                .build();
    }

}
