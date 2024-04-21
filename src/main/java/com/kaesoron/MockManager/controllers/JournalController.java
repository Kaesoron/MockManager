package com.kaesoron.MockManager.controllers;

import com.kaesoron.MockManager.dao.JournalDAO;
import com.kaesoron.MockManager.models.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalDAO journalDAO;

    @GetMapping
    public String showJournalPage(Model model,
                                  @RequestParam(name = "mockName", required = false) String mockName,
                                  @RequestParam(name = "mockId", required = false) Long mockId,
                                  @RequestParam(name = "method", required = false) String method,
                                  @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "25") int size,
                                  @RequestParam(name = "sort", defaultValue = "journalDateTime") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Journal> journals = journalDAO.search(mockName, mockId, method, date, pageable);

        model.addAttribute("journals", journals);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sizeOptions", Arrays.asList(10, 25, 50, 100));

        return "journal";
    }
}
