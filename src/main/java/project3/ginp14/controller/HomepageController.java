package project3.ginp14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomepageController {
    @GetMapping("/home")
    public String showHomepage(Principal principal){
        System.out.println(principal.getName());
        return "views/admin/fragments/master_layout";
    }

}
