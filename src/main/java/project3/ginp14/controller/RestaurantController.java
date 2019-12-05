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

    @GetMapping("/home")
    public String showDashboard(){
        return "views/restaurant/dashboard";
    }

    @GetMapping("/")
    public String showRestaurantInformation(Model model, Principal principal){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        int seatHold = bookingService.getTotalSeatHold(restaurant.getId());

        System.out.println(seatHold);
        model.addAttribute("seatNeed", seatHold);
        model.addAttribute("obj", restaurant);
        model.addAttribute("userFullName", user.getFullname());
        model.addAttribute("seatNeedProcess", bookingService.countAllByRestaurantAndVerifyStatus(restaurant, 0));
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
        return "redirect:/restaurant/";
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

    @GetMapping("/booking/edit")
    public String showEditForm(@RequestParam int id, Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Booking booking = bookingService.findById(id);
        booking.setBookingDatetime(booking.getBookingDatetime().replace(" ", "T"));
        if (isBookingOfCurrentRestaurant(user, id)){
            model.addAttribute("obj", booking);
            model.addAttribute("userFullName", user.getFullname());
            return "views/restaurant/booking/edit_booking";
        }
        else return "redirect:/restaurant/booking";
    }

    @PostMapping("/booking/edit")
    public String processEditBooking(@ModelAttribute("obj") Booking booking, Model model, Principal principal, HttpSession httpSession){
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        if (isBookingOfCurrentRestaurant(user, booking.getId())) {
            booking.setBookingDatetime(booking.getBookingDatetime().replace("T", " "));
            booking.setVerifyStatus(1);
            booking.setCheckStatus(1);
            bookingService.save(booking);
            return "redirect:/restaurant/booking";
        }
        return "redirect:/restaurant/booking";
    }

    @GetMapping("/booking/delete")
    public String deleteRestaurantType(@RequestParam int id, Model model, Principal principal){
        if (isBookingOfCurrentRestaurant(userDao.findByUsername(principal.getName()), id)) {
            try {
                Booking booking = bookingService.findById(id);
                booking.setVerifyStatus(3);
                bookingService.save(booking);
                String message = "Delete successfull !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", true);
            } catch (Exception e) {
                String message = "Delete failed !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", false);
            }
        }
        else {
            String message = "Delete failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:/restaurant/booking";
    }

    public boolean isBookingOfCurrentRestaurant(User user, int bookingId){
        // nếu booking thuộc restaurant
        if(restaurantService.findByUserId(user.getId()).getId() == bookingService.findById(bookingId).getRestaurant().getId()){
            return true;
        }
        return false;
    }

    @GetMapping("/booking/confirm-booking")
    public String confirmBooking(@RequestParam("bookingId") int id, Principal principal){
        User user = userDao.findByUsername(principal.getName());
        if (isBookingOfCurrentRestaurant(user, id)){
            Booking booking = bookingService.findById(id);
            booking.setVerifyStatus(1);
            bookingService.save(booking);
        }
        return "redirect:/restaurant/booking";
    }

    @GetMapping("/booking/confirm-arrived")
    public String confirmGuestArrived(@RequestParam("bookingId") int id, Principal principal){
        User user = userDao.findByUsername(principal.getName());
        if (isBookingOfCurrentRestaurant(user, id)){
            Booking booking = bookingService.findById(id);
            booking.setVerifyStatus(2);
            bookingService.save(booking);
        }
        return "redirect:/restaurant/booking";
    }
}
