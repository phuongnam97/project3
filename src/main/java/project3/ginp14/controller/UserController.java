package project3.ginp14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/register")
    public String showRegisterForm(){

        return "views/admin/register-form";
    }

    @GetMapping("/login")
    public String showLoginForm(){

        return "views/admin/login-form";
    }
}
