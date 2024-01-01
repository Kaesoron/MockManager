package com.kaesoron.MockManager.controller;

import com.kaesoron.MockManager.model.Errorinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Errorinfo processException(Exception e) {
        logger.error("Unexpected error", e);
        return new Errorinfo(e.getMessage());
    }
}
