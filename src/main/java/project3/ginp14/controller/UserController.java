package project3.ginp14.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project3.ginp14.dao.UserDao;
import project3.ginp14.entity.User;
import project3.ginp14.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "views/admin/register-form";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "views/admin/login-form";
    }

    @PostMapping("/process-register")
    public String processRegister(@ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        System.out.println("abcd");
        if(userService.checkExistInfo(user.getUsername(), user.getEmail(), user.getTelephone())) {
            System.out.println("Existed User have that username, email or telephone");
            return "redirect:register";
        } else {
            userService.save(user);
        }
        return "redirect:login";
    }
}
