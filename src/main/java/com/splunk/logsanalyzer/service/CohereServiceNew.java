package com.splunk.logsanalyzer.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class CohereServiceNew {
    private static final String API_URL = "https://api.cohere.ai/v1/generate";
    private static final String API_KEY = "HE7sjkc8BdreEKBXOdD0otKxi2llZuDvFyIlxtSg";  // <-- PUT YOUR KEY HERE

    public String askQuestion(String userQuestion, String splunkLogs) {
        RestTemplate restTemplate = new RestTemplate();

        // Compose the prompt (logs + question)
        String prompt = "You are an expert Splunk log analyst.\n"
                + "Here are the logs:\n"
                + splunkLogs + "\n\n"
                + "Question: " + userQuestion + "\n"
                + "Answer:";

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 300);
        requestBody.put("temperature", 0.3);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);

        // Extract generated answer
        Map<String, Object> responseBody = response.getBody();
        String answer = (String) responseBody.get("generation");
//        Map<String, Object> generations = (Map<String, Object>) responseBody.get("generations");
        ArrayList<Object> generations = (ArrayList<Object>) responseBody.get("generations");

//        ArrayList<Object> generationList = (ArrayList<Object>) generations.get(0);
        return ((Map<String, Object>) generations.get(0)).get("text").toString();
//        return answer;
    }
}
