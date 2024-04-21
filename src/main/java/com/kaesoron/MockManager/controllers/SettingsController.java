package com.kaesoron.MockManager.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.dao.MockDAO;
import com.kaesoron.MockManager.models.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping
    public String showSettingsPage(Model model,
                                   @RequestParam(name = "offset", defaultValue = "1") int offset,
                                   @RequestParam(name = "pageSize", defaultValue = "25") int pageSize,
                                   @RequestParam(name = "sortBy", defaultValue = "mockName") String sortBy) {
        Page<Mock> mocks = mockDAO.indexMocksWithPaginationAndSorting(offset - 1, pageSize, sortBy);
        int totalPages = (int) Math.ceil((double) mockDAO.index().size() / pageSize);
        model.addAttribute("mocks", mocks);
        model.addAttribute("currentPage", offset);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        return "settings";
    }

    @GetMapping("/new")
    public String newMock(Model model) {
        model.addAttribute("mock", new Mock());
        return "edit-mock";
    }

    @GetMapping("/edit/{mockId}")
    public String showEditMockPage(@PathVariable Long mockId, Model model) {
        Mock mock = mockDAO.read(mockId);
        model.addAttribute("mock", mock);
        return "edit-mock";
    }

    @PostMapping
    public String saveMock(@ModelAttribute Mock mock) {
        mockDAO.createOrUpdate(mock);
        return "redirect:/settings";
    }

    @PostMapping("/delete/{mockId}")
    public String deleteMock(@PathVariable Long mockId) {
        mockDAO.delete(mockId);
        return "redirect:/settings";
    }
}
