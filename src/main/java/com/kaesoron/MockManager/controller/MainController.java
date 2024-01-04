package com.kaesoron.MockManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaesoron.MockManager.model.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class MainController {
    private List<Endpoint> endpoints;
    private File endpointsToEdit = new File("src/main/resources/endpoints.json");
    ObjectMapper mapper = new ObjectMapper();


    public MainController() throws IOException {
        TypeReference<List<Endpoint>> typeReference = new TypeReference<>() {};
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("endpoints.json");
        endpoints = mapper.readValue(inputStream, typeReference);

    }

    @GetMapping("/settings")
    public ResponseEntity<String> getEndpoints() {
        try {
            JsonNode jsonNode = mapper.readTree(endpointsToEdit);
            return ResponseEntity.ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error reading endpoints list");
        }
    }

    @PostMapping("/settings")
    @PutMapping("/settings")
    public ResponseEntity<String> addEndpoint(@RequestBody Endpoint endpoint) {
        try {
            List<Endpoint> endpoints = mapper.readValue(endpointsToEdit, new TypeReference<>(){});
            endpoints.add(endpoint);
            mapper.writeValue(endpointsToEdit, endpoints);
            return ResponseEntity.ok("Endpoint added successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error adding endpoint");
        }
    }

    @DeleteMapping("/settings")
    public ResponseEntity<String> deleteEndpoint(@RequestBody Endpoint endpointToBeDeleted) {
        try {
            List<Endpoint> endpoints = mapper.readValue(endpointsToEdit, new TypeReference<>(){});
            endpoints.remove(endpointToBeDeleted);
            mapper.writerWithDefaultPrettyPrinter().writeValue(endpointsToEdit, endpoints);
            return ResponseEntity.ok("Endpoint deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting endpoint");
        }
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
