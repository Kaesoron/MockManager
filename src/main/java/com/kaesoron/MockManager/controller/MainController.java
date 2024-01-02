package com.kaesoron.MockManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaesoron.MockManager.model.Endpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value="")
public class MainController {
    private List<Endpoint> endpoints;

    public MainController() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Endpoint>> typeReference = new TypeReference<List<Endpoint>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/endpoints.json");
        endpoints = mapper.readValue(inputStream, typeReference);
    }

    @GetMapping(path = "{path}")
    public ResponseEntity<String> handleGetRequest(@PathVariable String path) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) ) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "{path}")
    public ResponseEntity<String> handlePostRequest(@PathVariable String path, @RequestBody String requestBody) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("POST")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "{path}")
    public ResponseEntity<String> handlePutRequest(@PathVariable String path, @RequestBody String requestBody) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("PUT")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "{path}")
    public ResponseEntity<String> handleDeleteRequest(@PathVariable String path) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("DELETE")) {
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
