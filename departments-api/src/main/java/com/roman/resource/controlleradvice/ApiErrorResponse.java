package com.roman.resource.controlleradvice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {

    private HttpStatus status;

    private String errorCode;

    private String message;

    private String detail;

    private LocalDateTime timeStamp;

    public static final class ApiErrorResponseBuilder {
        private HttpStatus status;
        private String errorCode;
        private String message;
        private String detail;
        private LocalDateTime timeStamp;

        public ApiErrorResponseBuilder() {}

        public static ApiErrorResponseBuilder anApiErrorResponse() {
            return new ApiErrorResponseBuilder();
        }

        public ApiErrorResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiErrorResponseBuilder withErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiErrorResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponseBuilder withDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public ApiErrorResponseBuilder atTime(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }
        public ApiErrorResponse build() {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.status = this.status;
            apiErrorResponse.errorCode = this.errorCode;
            apiErrorResponse.detail = this.detail;
            apiErrorResponse.message = this.message;
            apiErrorResponse.timeStamp = this.timeStamp;
            return apiErrorResponse;
        }
    }
}
