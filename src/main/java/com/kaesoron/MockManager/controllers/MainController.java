package com.kaesoron.MockManager.controllers;

import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import enums.Actions;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("")
@ConfigurationProperties(prefix = "endpoints")
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MockDAO mockDAO;
    @Autowired
    private JournalDAO journalDAO;

    @GetMapping("/**")
    @PostMapping("/**")
    @PutMapping("{path}")
    @DeleteMapping("{path}")
    public ResponseEntity<String> handleRequest(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst("/", "");
        Mock mock = mockDAO.readByRequest(path);

        if (path.equals(mock.getMockPath()) && mock.getMockMethod().equals(request.getMethod())) {
            try {
                TimeUnit.MILLISECONDS.sleep(mock.getMockTimeout());
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            journalDAO.create(mock, Actions.RESPONSE);
            return ResponseEntity.ok(mock.getMockResponse());
        } else {
            journalDAO.create(mock, Actions.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }
}
