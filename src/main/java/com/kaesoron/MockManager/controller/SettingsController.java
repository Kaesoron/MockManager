package com.kaesoron.MockManager.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private String endpointsFilePath;
    private File endpointsToEdit;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public SettingsController(Environment environment) throws IOException {
        endpointsFilePath = environment.getProperty("endpoints.filepath");
        assert endpointsFilePath != null;
        endpointsToEdit = new File(endpointsFilePath);
    }

    @GetMapping("")
    public String getEndpoints(Model model) {
        try {
            JsonNode jsonNode = mapper.readTree(endpointsToEdit);
            String jsonContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            model.addAttribute("endpointsJson", jsonContent);
            return "/settings";
        } catch (IOException e) {
            e.printStackTrace();
            return "/jsonReadError";
        }
    }

    @PostMapping("")
    public String addEndpoint(@RequestParam("endpointsJson") String endpointsJson, Model model) {
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configOverride(Map.class).setInclude(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL));
            Object endpoints = mapper.readValue(endpointsJson, Object.class);
            mapper.writeValue(endpointsToEdit, endpoints);
            model.addAttribute("endpointsJson", endpointsJson);
            model.addAttribute("message", "Endpoint list saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error adding endpoint");
        }
        return "/settings";
    }
}
