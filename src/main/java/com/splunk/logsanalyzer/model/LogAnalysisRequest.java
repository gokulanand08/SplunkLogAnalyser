package com.splunk.logsanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request to our Spring Boot API
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAnalysisRequest {
    private String query;
    // File will be handled separately in the controller
}
