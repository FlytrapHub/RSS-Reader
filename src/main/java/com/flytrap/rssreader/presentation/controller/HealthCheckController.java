package com.flytrap.rssreader.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping(value = "/api/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
