package com.splunk.logsanalyzer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CohereResponse {
    private String text;
    private List<Citation> citations;
    private Documents documents;
    private String id;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Citation {
        @JsonProperty("document_ids")
        private List<String> documentIds;
        private Integer start;
        private Integer end;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Documents {
        private List<DocInfo> cited;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DocInfo {
            private String id;
            private String text;
        }
    }
}
