package com.example.demospringsecurityform.form;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    private static final String MODEL_NAME = "message";

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute(MODEL_NAME, "Hello, Spring security!");
        } else {
            model.addAttribute(MODEL_NAME, "Hello, " + principal.getName());
        }
        
        
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute(MODEL_NAME, "This is info");
        return "info";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute(MODEL_NAME, "This is admin, " + principal.getName());
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute(MODEL_NAME, "This is dashboard, " + principal.getName());
        return "dashboard";
    }
}
