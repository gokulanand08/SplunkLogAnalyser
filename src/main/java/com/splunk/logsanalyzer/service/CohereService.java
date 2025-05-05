package com.splunk.logsanalyzer.service;

import com.splunk.logsanalyzer.model.CohereRequest;
import com.splunk.logsanalyzer.model.CohereResponse;
import com.splunk.logsanalyzer.model.LogAnalysisRequest;
import com.splunk.logsanalyzer.model.LogAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CohereService {

    private final WebClient webClient;

    @Value("${cohere.api.key}")
    private String apiKey;

    public CohereService(@Value("${cohere.api.url}") String apiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    public LogAnalysisResponse analyzeLogs(LogAnalysisRequest request, String logsContent) {
        try {
            // Parse logs content
            List<String> logLines = processLogsContent(logsContent);

            // Prepare request for Cohere
            CohereRequest cohereRequest = prepareCohereRequest(request.getQuery(), logLines);

            // Call Cohere API
            CohereResponse cohereResponse = callCohereApi(cohereRequest);

            // Map the response
            return mapToLogAnalysisResponse(cohereResponse);

        } catch (Exception e) {
            return LogAnalysisResponse.builder()
                    .status("ERROR")
                    .errorMessage("Error analyzing logs: " + e.getMessage())
                    .build();
        }
    }

    private List<String> processLogsContent(String logsContent) {
        if (logsContent == null || logsContent.isEmpty()) {
            return new ArrayList<>();
        }

        // Simple processing: split by newlines
        String[] lines = logsContent.split("\\r?\\n");
        List<String> logLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                logLines.add(line.trim());
            }
        }

        return logLines;
    }

    private CohereRequest prepareCohereRequest(String query, List<String> logLines) {
        List<CohereRequest.Document> documents = new ArrayList<>();

        // Create documents from log lines
        // We'll use a single document for simplicity, but you could split into multiple if needed
        StringBuilder sb = new StringBuilder();
        for (String line : logLines) {
            sb.append(line).append("\n");
        }

        if (sb.length() > 0) {
            documents.add(new CohereRequest.Document(sb.toString(), "splunk_logs"));
        }

        // Create preamble with instructions
        CohereRequest.Preamble preamble = new CohereRequest.Preamble(
                "You are an expert at analyzing Splunk logs. Provide insights, identify patterns, " +
                        "flag errors, and explain what's happening in these logs. Be concise but thorough."
        );

        // Build the request
        return CohereRequest.builder()
                .message(query)
                .documents(documents)
                .preamble(preamble)
                .build();
    }

    private CohereResponse callCohereApi(CohereRequest request) {
        return webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CohereResponse.class)
                .block();
    }

    private LogAnalysisResponse mapToLogAnalysisResponse(CohereResponse cohereResponse) {
        if (cohereResponse == null) {
            return LogAnalysisResponse.builder()
                    .status("ERROR")
                    .errorMessage("No response received from Cohere API")
                    .build();
        }

        return LogAnalysisResponse.builder()
                .analysis(cohereResponse.getText())
                .citations(cohereResponse.getCitations())
                .sourceLogs(cohereResponse.getDocuments() != null ? cohereResponse.getDocuments().getCited() : null)
                .requestId(cohereResponse.getId() != null ? cohereResponse.getId() : UUID.randomUUID().toString())
                .status("SUCCESS")
                .build();
    }
}
