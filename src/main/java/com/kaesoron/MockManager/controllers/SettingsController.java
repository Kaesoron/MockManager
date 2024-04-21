package com.kaesoron.MockManager.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private MockDAO mockDAO;

    @GetMapping("")
    public List<Mock> getMocks(Model model) {
        return mockDAO.index();
    }

    @GetMapping("/{sortBy}")
    public List<Mock> getMocksWithSorting(@PathVariable String sortBy) {
        return mockDAO.indexWithSorting(sortBy);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<Mock> getMocksWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        return mockDAO.indexMocksWithPagination(offset, pageSize);
    }

    @GetMapping("/pagination/{offset}/{pageSize}/{sortBy}")
    public Page<Mock> getMocksWithPaginationAndSorting(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String sortBy) {
        return mockDAO.indexMocksWithPaginationAndSorting(offset, pageSize, sortBy);
    }

    @PostMapping("")
    public String addMock(Mock mock) {
        List<Mock> mocks = mockDAO.index();

        for (Mock contained : mocks) {
            if (contained.getMockPath().equals(mock.getMockPath())
            && contained.getMockMethod().equals(mock.getMockMethod())) {
                return "/mockAlreadyExists";
            }
        }
        mockDAO.create(mock);
        return "/settings";
    }
}
