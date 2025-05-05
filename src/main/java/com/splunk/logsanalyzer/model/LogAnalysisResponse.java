package com.splunk.logsanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAnalysisResponse {
    private String analysis;
    private List<CohereResponse.Citation> citations;
    private List<CohereResponse.Documents.DocInfo> sourceLogs;
    private String requestId;
    private String status;
    private String errorMessage;
}
