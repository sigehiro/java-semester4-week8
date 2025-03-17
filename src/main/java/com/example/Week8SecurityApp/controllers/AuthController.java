package com.example.Week8SecurityApp.controllers;

import com.example.Week8SecurityApp.models.MyUser;
import com.example.Week8SecurityApp.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController implements ErrorController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Handle access denied errors and redirect to a custom 403 error page
    @GetMapping("/error")
    public String error403() {
        return "Auth/error403";
    }

    //Display the login page for authentication
    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        return "Auth/login";
    }


    // Handle logout and redirect to the home page
//    @GetMapping("/logout")
//    public String logout() {
//        return "home";
//    }

    //customer registration - open the registration page
    @GetMapping("/register")
    public String register(Model model, @RequestParam (required = false) String message) {
        model.addAttribute("restaurantName", message);
        model.addAttribute("user", new MyUser());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") MyUser user) {
        //save the user to the database
        int statusCode = userService.saveUser(user);
        if (statusCode == 0) {
            return "redirect:/register?message=User already exists";
        } else {
            return "redirect:/login?message=User registered successfully";
        }
    }
}
