package project3.ginp14.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project3.ginp14.dao.UserDao;
import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.RestaurantType;
import project3.ginp14.entity.User;
import project3.ginp14.service.BookingService;
import project3.ginp14.service.RestaurantService;
import project3.ginp14.service.RestaurantTypeService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantTypeService restaurantTypeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserDao userDao;

    @GetMapping("/dashboard")
    public String showDashboard(){
        return "views/restaurant/dashboard";
    }

    @GetMapping("/info")
    public String showRestaurantInformation(Model model, Principal principal){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        model.addAttribute("obj", restaurant);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/dashboard";
    }

    @GetMapping("/info/edit")
    public String showFormEditRestaurantInformation(Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        List<RestaurantType> restaurantTypes = restaurantTypeService.findAll();

        System.out.println(restaurant.getId());
        model.addAttribute("restaurantTypes",restaurantTypes);
        model.addAttribute("obj", restaurant);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/restaurantinfo/edit_restaurant_info";
    }

    @PostMapping("/info/edit")
    public String processEditRestaurantInformation(@ModelAttribute("obj") Restaurant restaurant, Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant1 = restaurantService.findByUserId(user.getId());
        restaurant.setId(restaurant1.getId());
        restaurant.setUser(user);

        restaurantService.save(restaurant);
        return "redirect:/restaurant/info";
    }

    @GetMapping("/booking")
    public String showListBooking(Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        List<Booking> listBooking = bookingService.findAllByRestaurantId(restaurant);

        model.addAttribute("userFullname", user.getFullname());
        model.addAttribute("isEmpty", listBooking.isEmpty());
        model.addAttribute("listObj",listBooking);
        return "views/restaurant/booking/list_booking";
    }

    @GetMapping("/booking/create")
    public String showFormCreateBooking(Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Booking booking = new Booking();
        model.addAttribute("obj", booking);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/booking/create_booking";
    }

    @PostMapping("/booking/create")
    public String processCreateBooking(@ModelAttribute("obj") Booking booking, Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        booking.setBookingDatetime(booking.getBookingDatetime().replace("T"," "));
        booking.setRestaurant(restaurant);
        booking.setCreatedUser(user);
        booking.setVerifyStatus(1);
        booking.setCheckStatus(1);
        bookingService.save(booking);
        return "redirect:/restaurant/booking";
    }
}
