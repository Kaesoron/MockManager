package com.kaesoron.MockManager.controllers;

import com.kaesoron.MockManager.models.Mock;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mockAlreadyExists")
public class ErrorController {

    @GetMapping("")
    public void mockAlreadyExists(Model model) {
    }
}
