package com.devvault.devvault.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v1/test")
    public String test() {
        return "You are authenticated";
    }
    @GetMapping("/api/v1/admin/test")
    public String adminTest() {
        return "You are an admin";
    }
}