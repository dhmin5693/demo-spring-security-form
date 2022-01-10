package com.example.demospringsecurityform.form;

import com.example.demospringsecurityform.account.Account;
import com.example.demospringsecurityform.account.UserAccount;
import com.example.demospringsecurityform.common.SecurityLogger;
import com.example.demospringsecurityform.config.CurrentUser;
import java.util.concurrent.Callable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String index(Model model, @CurrentUser Account account) {
        if (account == null) {
            model.addAttribute(MODEL_NAME, "Hello, Spring security!");
        } else {
            model.addAttribute(MODEL_NAME, "Hello, " + account.getUsername());
        }

        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute(MODEL_NAME, "This is info");
        return "info";
    }

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal UserAccount userAccount) {
        model.addAttribute(MODEL_NAME, "This is admin, " + userAccount.getAccount().getUsername());
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserAccount userAccount) {
        model.addAttribute(MODEL_NAME, "This is dashboard, " + userAccount.getAccount().getUsername());
        sampleService.dashboard();
        return "dashboard";
    }

    @GetMapping("/user")
    public String user(Model model, @AuthenticationPrincipal UserAccount userAccount) {
        model.addAttribute(MODEL_NAME, "This is user, " + userAccount.getAccount().getUsername());
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

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService() {
        SecurityLogger.log("MVC, before async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC, after async service");
        return "Async Handler";
    }
}
