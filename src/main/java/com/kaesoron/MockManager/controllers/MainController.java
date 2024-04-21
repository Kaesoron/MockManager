package com.kaesoron.MockManager.controllers;

import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import enums.Actions;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("")
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MockDAO mockDAO;
    @Autowired
    private JournalDAO journalDAO;

    @RequestMapping(value = "**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> handleRequest(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst("/", "");
        String method = request.getMethod();

        Optional<Mock> mockOptional = mockDAO.readByRequest(path, method);

        if (mockOptional.isPresent()) {
            Mock mock = mockOptional.get();
            if (path.equals(mock.getMockPath()) && mock.getMockMethod().equals(request.getMethod())) {
                try {
                    TimeUnit.MILLISECONDS.sleep(mock.getMockTimeout());
                    journalDAO.create(mock, Actions.RESPONSE);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                    Thread.currentThread().interrupt();
                    journalDAO.create(mock, Actions.NOT_FOUND);
                }
                journalDAO.create(mock, Actions.NOT_FOUND);
                return ResponseEntity.ok(mock.getMockResponse());
            }
        }

        journalDAO.create(new Mock(), Actions.NOT_FOUND);
        return ResponseEntity.notFound().build();
    }
}
