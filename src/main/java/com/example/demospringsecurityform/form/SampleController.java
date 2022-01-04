package com.example.demospringsecurityform.form;

import com.example.demospringsecurityform.common.SecurityLogger;
import java.security.Principal;
import java.util.concurrent.Callable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    private static final String MODEL_NAME = "message";

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

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
        sampleService.dashboard();
        return "dashboard";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute(MODEL_NAME, "This is user, " + principal.getName());
        return "user";
    }

    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler() {
        SecurityLogger.log("MVC");
        return () -> {
            SecurityLogger.log("Callable");
            return "Async Handler";
        };
    }
}
