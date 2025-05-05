# Splunk Logs Analyzer with Spring Boot Proxy

This project provides a Spring Boot backend proxy for the Splunk Logs Analyzer application. It resolves CORS issues by handling API calls to Cohere through a backend server instead of making them directly from the browser.

## Project Overview

This application allows users to:
- Enter a text query about Splunk logs
- Upload a Splunk logs file
- Get AI-powered analysis of the logs using the Cohere RAG API

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Quick Start

1. Clone or download this project
2. Open a terminal in the project directory
3. Run the application:
   ```
   mvn spring-boot:run
   ```
4. Open your browser and go to http://localhost:8080

## How It Works

1. The web frontend collects the user query and log file content
2. It sends this data to the Spring Boot backend API
3. The backend securely forwards the request to the Cohere RAG API
4. The response from Cohere is returned to the user

## Project Structure

```
cohere-proxy/
├── pom.xml                    # Project configuration and dependencies
├── src/
│   ├── main/
│   │   ├── java/com/example/cohereproxy/
│   │   │   ├── CohereProxyApplication.java   # Application entry point
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java            # CORS configuration
│   │   │   ├── controller/
│   │   │   │   ├── CohereProxyController.java    # API endpoint handler
│   │   │   │   └── StaticResourceController.java # Frontend controller
│   │   │   ├── model/
│   │   │   │   └── CohereRequest.java        # Request data model
│   │   │   └── service/
│   │   │       └── CohereProxyService.java   # Business logic & API calls
│   │   └── resources/
│   │       ├── application.properties        # Configuration file
│   │       └── static/
│   │           └── index.html                # Web frontend
│   └── test/
```

## Configuration

The API key is configured in `src/main/resources/application.properties`. You can modify this file if you need to:

```properties
server.port=8080
cohere.api.key=
```

## Customization

- **Change server port**: Modify `server.port` in `application.properties`
- **Update API key**: Modify `cohere.api.key` in `application.properties`
- **Modify the frontend**: Edit `src/main/resources/static/index.html`

## Troubleshooting

- **Server won't start**: Make sure port 8080 is available or change it in the properties
- **Connection errors**: Check your internet connection and API key
- **File reading errors**: Make sure your Splunk log file is valid and not corrupted
