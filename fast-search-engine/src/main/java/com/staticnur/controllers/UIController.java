package com.staticnur.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/search")
    public String home(Model model) {
        return "searchAddress";
    }
}
