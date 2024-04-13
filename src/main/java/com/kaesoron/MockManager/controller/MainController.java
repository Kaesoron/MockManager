package com.kaesoron.MockManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaesoron.MockManager.model.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("")
@ConfigurationProperties(prefix = "endpoints")
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper();
    private final List<Endpoint> endpoints;
    private final String endpointsFilePath;
    private final String loggerProcessed = "PROCESSED REQUEST: method: {}, path: {}, delay: {}";

    @Autowired
    public MainController(Environment environment) throws IOException {
        endpointsFilePath = environment.getProperty("endpoints.filepath");
        assert endpointsFilePath != null;
        TypeReference<List<Endpoint>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(endpointsFilePath.substring(endpointsFilePath.lastIndexOf("/") + 1));
        endpoints = mapper.readValue(inputStream, typeReference);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView redirectToSettings() {
        return new ModelAndView("redirect:/settings");
    }

    @GetMapping("/**")
    public ResponseEntity<String> handleGetRequest(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst("/", "");
        for (Endpoint endpoint : endpoints) {
            if (endpoint.path().equals(path) && endpoint.method().equalsIgnoreCase("GET")) {
                try {
                    TimeUnit.MILLISECONDS.sleep(endpoint.timeout());
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info(loggerProcessed, "GET", path, endpoint.timeout());
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
                try {
                    TimeUnit.MILLISECONDS.sleep(endpoint.timeout());
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info(loggerProcessed, "POST", path, endpoint.timeout());
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
                try {
                    TimeUnit.MILLISECONDS.sleep(endpoint.timeout());
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info(loggerProcessed, "PUT", path, endpoint.timeout());
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
                try {
                    TimeUnit.MILLISECONDS.sleep(endpoint.timeout());
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info(loggerProcessed, "DELETE", path, endpoint.timeout());
                return ResponseEntity.ok(endpoint.response());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
