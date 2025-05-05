package com.splunk.logsanalyzer.controller;

import com.splunk.logsanalyzer.model.LogAnalysisRequest;
import com.splunk.logsanalyzer.model.LogAnalysisResponse;
import com.splunk.logsanalyzer.service.CohereService;
import com.splunk.logsanalyzer.service.CohereServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")  // Allow cross-origin requests for frontend integration
public class LogAnalyzerController {

    private final CohereService cohereService;
    @Autowired
    private CohereServiceNew cohereServiceNew;

    @Autowired
    public LogAnalyzerController(CohereService cohereService) {
        this.cohereService = cohereService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LogAnalysisResponse> analyzeLogs(
            @RequestParam("query") String query,
            @RequestParam("file") MultipartFile file) {

        try {
            // Read file content
            String logsContent = new String(file.getBytes(), StandardCharsets.UTF_8);

            // Create request object
            LogAnalysisRequest request = new LogAnalysisRequest();
            request.setQuery(query);

            // Process the request
//            LogAnalysisResponse response = cohereService.analyzeLogs(request, logsContent);
            String answer = cohereServiceNew.askQuestion(query, logsContent);
            LogAnalysisResponse response = LogAnalysisResponse.builder()
                    .analysis(answer)
//                    .citations(cohereResponse.getCitations())
//                    .sourceLogs(cohereResponse.getDocuments() != null ? cohereResponse.getDocuments().getCited() : null)
//                    .requestId(cohereResponse.getId() != null ? cohereResponse.getId() : UUID.randomUUID().toString())
                    .status("SUCCESS")
                    .build();


            return ResponseEntity.ok(response);

        } catch (IOException e) {
            LogAnalysisResponse errorResponse = LogAnalysisResponse.builder()
                    .status("ERROR")
                    .errorMessage("Failed to read uploaded file: " + e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }
}
