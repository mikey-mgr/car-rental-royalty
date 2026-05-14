package com.Mike.Proj.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping({"/health", "/healthz"})
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("status", "healthy");
        payload.put("service", "backend");
        payload.put("message", "Service is healthy");
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}
