package com.project.hamburger_weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alligatoah
 */


@RestController
public class HelloController {

    @GetMapping
    public String home() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/something")
    public String something() {
        System.out.println("we are clicking something");
        return "Hello, Something!";
    }
} 