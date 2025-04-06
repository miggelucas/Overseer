package com.overseer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Wellcome to Overseer";
    }

    @GetMapping("/greetings/{name}")
    public String greetings(@PathVariable String name) {
        return "Hello, " + name + "!" + " Welcome to Overseer!";
    }
}
