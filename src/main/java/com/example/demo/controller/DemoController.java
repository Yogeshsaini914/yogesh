package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/user/hello")
    public String userHello() {
        return "Hello User or Admin!";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hello Admin!";
    }
}
