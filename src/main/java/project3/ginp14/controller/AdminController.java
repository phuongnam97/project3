package project3.ginp14.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project3.ginp14.entity.RestaurantType;
import project3.ginp14.service.RestaurantTypeService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    RestaurantTypeService restaurantTypeService;

    @GetMapping("/restaurant_type")
    public String showListRestaurantType(Model model){
        List<RestaurantType> listObj = restaurantTypeService.findAll();
        if (listObj.isEmpty()){
            model.addAttribute("isEmpty",true);
        } else {
            model.addAttribute("isEmpty",false);
        }
        RestaurantType restaurantType = new RestaurantType();
        model.addAttribute("obj", restaurantType);
        model.addAttribute("listObj", listObj);
        return "views/admin/restauranttype/list_restaurant_type";
    }

    @GetMapping("/restaurant_type/create")
    public String showFormCreateRestaurantType(Model model){
        RestaurantType restaurantType = new RestaurantType();
        model.addAttribute("obj", restaurantType);
        return "views/admin/restauranttype/create_restaurant_type";
    }

    @PostMapping("/restaurant_type/create")
    public String createNewRestaurantType(@ModelAttribute("obj") RestaurantType restaurantType, BindingResult bindingResult, Model model){
        try {
            restaurantTypeService.save(restaurantType);
            String message = "Create successfull !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", true);
        } catch (Exception e){
            String message = "Create failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:";
    }

    @GetMapping("/restaurant_type/edit")
    public String showFormEditRestaurantType(@RequestParam int id, Model model){
        RestaurantType restaurantType = restaurantTypeService.findById(id);
        model.addAttribute("obj", restaurantType);
        return "views/admin/restauranttype/edit_restaurant_type";
    }

    @PostMapping("/restaurant_type/edit")
    public String editRestaurantType(@ModelAttribute("obj") RestaurantType restaurantType, BindingResult bindingResult, Model model){
        try {
            restaurantTypeService.save(restaurantType);
            String message = "Edit successfull !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", true);
        } catch (Exception e){
            String message = "Edit failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:";
    }

    @GetMapping("/restaurant_type/delete")
    public String deleteRestaurantType(@RequestParam int id, Model model){
        try{
            restaurantTypeService.deleteById(id);
            String message = "Delete successfull !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", true);
        } catch (Exception e){
            String message = "Edit failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:";
    }
}
