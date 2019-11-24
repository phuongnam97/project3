package project3.ginp14.controller;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.RestaurantType;
import project3.ginp14.entity.User;
import project3.ginp14.service.BookingService;
import project3.ginp14.service.RestaurantService;
import project3.ginp14.service.RestaurantTypeService;
import project3.ginp14.service.UserService;
import project3.ginp14.utils.MailSender;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Controller
public class HomepageController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantTypeService restaurantTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    public int getBookingTime(String username){
        return bookingService.countAllByGuestTelephone(userService.findUserbyUsername(username).getTelephone());
    }

    @GetMapping("")
    public String showHomepage(Model model, Principal principal){
        MailSender mailSender = new MailSender();
        mailSender.sendMail();
        model.addAttribute("listRestaurantType", getListRestaurantType());
        try {
            model.addAttribute("bookingTime", getBookingTime(principal.getName()));
        } catch (Exception ex){}
        return "views/user/homepage";
    }

    @GetMapping("/login-successful")
    public String processLogin(Principal principal){
        User currentUser = userService.findUserbyUsername(principal.getName());
        if (currentUser.getRole().getName().equals("Role_User")){
            return "redirect:/";
        }
        if (currentUser.getRole().getName().equals("Role_Restaurant")){
            return "redirect:/restaurant/";
        }
        if (currentUser.getRole().getName().equals("Role_Admin")){
            return "redirect:/admin/users";
        }
        return "redirect:/home";
    }

    @GetMapping("/list-restaurant")
    public String showRestaurantByRestaurantType(@RequestParam int restaurantTypeId, Model model, Principal principal, @PageableDefault(size = 6) Pageable pageable){
        Page<Restaurant> listRestaurant;
        if (restaurantTypeId == 0){
            listRestaurant = restaurantService.findAll(pageable);
        }
        else {listRestaurant = restaurantService.findAllByRestaurantType(restaurantTypeService.findById(restaurantTypeId), pageable);}
        model.addAttribute("listRestaurantType", getListRestaurantType());
        model.addAttribute("listRestaurant", listRestaurant);
        model.addAttribute("restaurantTypeId", restaurantTypeId);
        return "views/user/list-restaurant";
    }

    @GetMapping("/restaurant")
    public String showRestaurantDetail(@RequestParam int id, Model model){
        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("obj", restaurant);
        model.addAttribute("listRestaurantType", getListRestaurantType());
        return "views/user/restaurant-detail";
    }

    public List<RestaurantType> getListRestaurantType(){
        List<RestaurantType> listRestaurantType = restaurantTypeService.findAll();
        return listRestaurantType;
    }

    @GetMapping("/booking")
    public String showFormCreateBooking(@RequestParam int restaurantId, Model model, Principal principal){
        User currentUser = new User();
        try {
            currentUser = userService.findUserbyUsername(principal.getName());
        } catch (Exception e){
            return "redirect:/restaurant?id=" + restaurantId;
        }

        Restaurant restaurant = restaurantService.findById(restaurantId);
        Booking booking = new Booking();
        model.addAttribute("obj", booking);
        model.addAttribute("user_id", currentUser.getId());
        System.out.println(currentUser.getFullname() + " - " + currentUser.getTelephone());
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("listRestaurantType", getListRestaurantType());
        return "views/user/booking";
    }

    @PostMapping("/booking")
    public String processCreateBooking(@ModelAttribute("obj") Booking booking, Model model, Principal principal) {
        User user = userService.findUserbyUsername(principal.getName());
        booking.setGuestName(user.getFullname());
        booking.setGuestTelephone(user.getTelephone());
        booking.setCreatedUser(user);
        booking.setVerifyStatus(0);
        booking.setCheckStatus(0);
        booking.setBookingDatetime(booking.getBookingDatetime().replace("T"," "));
        bookingService.save(booking);
        return "redirect:/";
    }

    @GetMapping("/booking-history")
    public String showBookingHistory(Model model, Principal principal, @PageableDefault(size = 10) Pageable pageable){
        try {
            System.out.println(principal.getName());
        } catch (Exception e){ return "redirect:/";}
        if (principal.getName().equals("anonymousUser")){
            return "redirect:/";
        }
        User user = userService.findUserbyUsername(principal.getName());
        Page<Booking> listObj = bookingService.findAllByGuestNameAndGuestTelephone(user.getFullname(), user.getTelephone(), pageable);
        model.addAttribute("listObj", listObj);
        return "views/user/booking-history";
    }
}
