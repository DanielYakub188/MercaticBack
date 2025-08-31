package com.mercatic.mercaticBack.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controlador1 {
    @GetMapping("/hello")
    public String hello() {
        return "Hola desde Spring Boot 🚀";
    }
}
