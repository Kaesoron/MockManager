package com.kaesoron.MockManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaesoron.MockManager.model.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("")
public class MainController {
    private List<Endpoint> endpoints;

    public MainController() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Endpoint>> typeReference = new TypeReference<>() {};
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("endpoints.json");
        endpoints = mapper.readValue(inputStream, typeReference);
    }

    @GetMapping("/**")
    public ResponseEntity<String> handleGetRequest(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst("/", "");
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("GET")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/**")
    public ResponseEntity<String> handlePostRequest(HttpServletRequest request, @RequestBody String requestBody) {
        String path = request.getRequestURI().replaceFirst("/", "");
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("POST")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{path}")
    public ResponseEntity<String> handlePutRequest(HttpServletRequest request, @RequestBody String requestBody) {
        String path = request.getRequestURI().replaceFirst("/", "");
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("PUT")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{path}")
    public ResponseEntity<String> handleDeleteRequest(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst("/", "");
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("DELETE")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
