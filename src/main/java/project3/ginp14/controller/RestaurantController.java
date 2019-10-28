package project3.ginp14.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.service.RestaurantService;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/info")
    public String showRestaurantInformation(@RequestParam int id, Model model){
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("obj", restaurant);
        return "views/restaurant/dashboard";
    }

    @GetMapping("/info/edit")
    public String editRestaurantInfomation(@RequestParam int id, Model model){
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("obj", restaurant);
        return "views/restaurant/restaurantinfo/edit_restaurant_info";
    }
}
