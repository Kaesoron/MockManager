package com.kaesoron.MockManager.model;

public record Endpoint(
        String path,
        String method,
        String response
) {}
