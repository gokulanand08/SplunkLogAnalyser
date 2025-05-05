package com.splunk.logsanalyzer.exception;

import com.splunk.logsanalyzer.model.LogAnalysisResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<LogAnalysisResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
        LogAnalysisResponse response = LogAnalysisResponse.builder()
                .status("ERROR")
                .errorMessage("File size exceeds the maximum allowed size")
                .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<LogAnalysisResponse> handleWebClientResponseException(WebClientResponseException e) {
        LogAnalysisResponse response = LogAnalysisResponse.builder()
                .status("ERROR")
                .errorMessage("Error from Cohere API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LogAnalysisResponse> handleGenericException(Exception e) {
        LogAnalysisResponse response = LogAnalysisResponse.builder()
                .status("ERROR")
                .errorMessage("An unexpected error occurred: " + e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
