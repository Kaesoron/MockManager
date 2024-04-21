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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        int pageIndex = Math.max(0, offset - 1);
        Page<Mock> mocks = mockDAO.indexMocksWithPaginationAndSorting(pageIndex, pageSize, sortBy);

        model.addAttribute("mocks", mocks.getContent());
        model.addAttribute("currentPage", offset);
        model.addAttribute("totalPages", mocks.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sizeOptions", Arrays.asList(10, 25, 50, 100)); // Используем Arrays.asList для Java 8 и выше

        return "settings";
    }

    @GetMapping("/new")
    public String newMock(Model model) {
        model.addAttribute("mock", new Mock());
        return "edit-mock";
    }

    @GetMapping("/edit/{mockId}")
    public String showEditMockPage(@PathVariable("mockId") Long mockId, Model model) {
        Optional<Mock> optionalMock = mockDAO.read(mockId);
        if (!optionalMock.isPresent()) {
            // Обработка случая, когда Mock не найден
            return "redirect:/settings"; // Перенаправить на список заглушек
        } else{
            model.addAttribute("mock", optionalMock.get());
            // Здесь мы передаем Mock, а не Optional
            return "edit-mock";
        }
    }

    @PostMapping("/save")
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
