package project3.ginp14.controller;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project3.ginp14.config.CloudinaryConfig;
import project3.ginp14.dao.UserDao;
import project3.ginp14.entity.*;
import project3.ginp14.service.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TableService tableService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishTypeService dishTypeService;

    @Autowired
    private CloudinaryConfig cloudinary;

    @Autowired
    private OrderService orderService;

    // Dashboard
    @GetMapping("/home")
    public String showDashboard() {
        return "views/restaurant/dashboard";
    }

    @GetMapping("/")
    public String showRestaurantInformation(Model model, Principal principal) {
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


    // Infomation edit
    @GetMapping("/info/edit")
    public String showFormEditRestaurantInformation(Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        List<RestaurantType> restaurantTypes = restaurantTypeService.findAll();

        System.out.println(restaurant.getId());
        model.addAttribute("restaurantTypes", restaurantTypes);
        model.addAttribute("obj", restaurant);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/restaurantinfo/edit_restaurant_info";
    }

    @PostMapping("/info/edit")
    public String processEditRestaurantInformation(@ModelAttribute("obj") Restaurant restaurant, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant1 = restaurantService.findByUserId(user.getId());
        restaurant.setId(restaurant1.getId());
        restaurant.setUser(user);

        restaurantService.save(restaurant);
        return "redirect:/restaurant/";
    }


    //Booking management
    @GetMapping("/booking")
    public String showListBooking(Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        List<Booking> listBooking = bookingService.findAllByRestaurantId(restaurant);

        model.addAttribute("userFullname", user.getFullname());
        model.addAttribute("isEmpty", listBooking.isEmpty());
        model.addAttribute("listObj", listBooking);
        return "views/restaurant/booking/list_booking";
    }

    @GetMapping("/booking/create")
    public String showFormCreateBooking(Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Booking booking = new Booking();
        model.addAttribute("obj", booking);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/booking/create_booking";
    }

    @PostMapping("/booking/create")
    public String processCreateBooking(@ModelAttribute("obj") Booking booking, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        booking.setBookingDatetime(booking.getBookingDatetime().replace("T", " "));
        booking.setRestaurant(restaurant);
        booking.setCreatedUser(user);
        booking.setVerifyStatus(1);
        booking.setCheckStatus(1);
        bookingService.save(booking);
        return "redirect:/restaurant/booking";
    }

    @GetMapping("/booking/edit")
    public String showEditForm(@RequestParam int id, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Booking booking = bookingService.findById(id);
        System.out.println(booking.getCheckStatus());
        System.out.println(booking.getVerifyStatus());
        booking.setBookingDatetime(booking.getBookingDatetime().replace(" ", "T"));
        List<Dish> listDish = dishService.findByRestaurant(booking.getRestaurant());
        List<Order> listOrder = orderService.findByBooking(booking);
        if (isBookingOfCurrentRestaurant(user, id)) {
            model.addAttribute("obj", booking);
            model.addAttribute("userFullName", user.getFullname());
            model.addAttribute("listDish", listDish);
            model.addAttribute("listItem", listOrder);
            return "views/restaurant/booking/edit_booking";
        } else return "redirect:/restaurant/booking";
    }

    @PostMapping("/booking/edit")
    public String processEditBooking(@ModelAttribute("obj") Booking booking, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        if (isBookingOfCurrentRestaurant(user, booking.getId())) {
            booking.setBookingDatetime(booking.getBookingDatetime().replace("T", " "));
            booking.setCheckStatus(1);
            bookingService.save(booking);
            return "redirect:/restaurant/booking";
        }
        return "redirect:/restaurant/booking";
    }

    @GetMapping("/booking/delete")
    public String deleteRestaurantType(@RequestParam int id, Model model, Principal principal) {
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
        } else {
            String message = "Delete failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:/restaurant/booking";
    }

    public boolean isBookingOfCurrentRestaurant(User user, int bookingId) {
        // nếu booking thuộc restaurant
        if (restaurantService.findByUserId(user.getId()).getId() == bookingService.findById(bookingId).getRestaurant().getId()) {
            return true;
        }
        return false;
    }

    @GetMapping("/booking/confirm-booking")
    public String confirmBooking(@RequestParam("bookingId") int id, Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        if (isBookingOfCurrentRestaurant(user, id)) {
            Booking booking = bookingService.findById(id);
            booking.setVerifyStatus(1);
            bookingService.save(booking);
        }
        return "redirect:/restaurant/booking";
    }

    @GetMapping("/booking/confirm-arrived")
    public String confirmGuestArrived(@RequestParam("bookingId") int id, Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        if (isBookingOfCurrentRestaurant(user, id)) {
            Booking booking = bookingService.findById(id);
            booking.setVerifyStatus(2);
            bookingService.save(booking);
        }
        return "redirect:/restaurant/booking";
    }


    // Table management
    @GetMapping("/table")
    public String showListTable(Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        model.addAttribute("userFullname", user.getFullname());
        List<Table> listObj = tableService.findByRestaurant(restaurant);
        model.addAttribute("listObj", listObj);
        model.addAttribute("userFullName", user.getFullname());
        return "views/restaurant/table/list_table";
    }

    @GetMapping("/table/create")
    public String showFormCreateTable(Model model, Principal principal, HttpSession httpSession) {
        Table table = new Table();
        model.addAttribute("obj", table);
        return "views/restaurant/table/create_table";
    }

    @PostMapping("/table/create")
    public String processCreateTable(@ModelAttribute("obj") Table table, @RequestParam("no_of_table") int quantity, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        table.setRestaurant(restaurant);
        String code = "";
        code += "BAN";
        code += restaurant.getId();
        code += table.getQuantity();
        int number = 0;
        Table table0 = tableService.findTopByCodeContains("%" + code + "%");

        if (table0 != null) {
            number = Integer.parseInt(table0.getCode().replace(code, ""));
        }

        for (int i = 0; i < quantity; i++) {
            Table table1 = new Table();
            table1.setCode(code + (number + i + 1));
            table1.setRestaurant(restaurant);
            table1.setDescription(table.getDescription());
            table1.setQuantity(table.getQuantity());
            tableService.save(table1);
        }
        System.out.println(quantity);
        System.out.println(table.getDescription());
        return "redirect:/restaurant/table";
    }

    @GetMapping("/table/delete")
    public String deleteTable(@RequestParam int id, Model model, Principal principal) {
        if (isTableOfCurrentRestaurant(userDao.findByUsername(principal.getName()), id)) {
            try {
                Table table = tableService.findById(id);
                tableService.deleteById(id);
                String message = "Delete successfull !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", true);
            } catch (Exception e) {
                String message = "Delete failed !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", false);
            }
        } else {
            String message = "Delete failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:/restaurant/table";
    }

    public boolean isTableOfCurrentRestaurant(User user, int id) {
        if (restaurantService.findByUserId(user.getId()).getId() == tableService.findById(id).getRestaurant().getId()) {
            return true;
        }
        return false;
    }


    // Dish Management
    @GetMapping("/dish")
    public String showListDish(Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        List<Dish> listDish = dishService.findByRestaurant(restaurant);

        model.addAttribute("userFullname", user.getFullname());
        model.addAttribute("listObj", listDish);
        return "views/restaurant/dish/list_dish";
    }

    @GetMapping("/dish/create")
    public String showFormCreateDish(Model model, Principal principal, HttpSession httpSession) {
        Dish dish = new Dish();
        List<DishType> listDishType = dishTypeService.findAll();
        model.addAttribute("obj", dish);
        model.addAttribute("listDishType", listDishType);
        return "views/restaurant/dish/create_dish";
    }

    @PostMapping("/dish/create")
    public String processCreateDish(@ModelAttribute("obj") Dish dish, @RequestParam("file") MultipartFile file, Model model, Principal principal, HttpSession httpSession) {
        try {
            User user = userDao.findByUsername(principal.getName());
            Restaurant restaurant = restaurantService.findByUserId(user.getId());
            dish.setRestaurant(restaurant);
            Map uploadResult = cloudinary.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            String url = uploadResult.get("url").toString();
            System.out.println(url);
            dish.setImage(url);
            dishService.save(dish);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/restaurant/dish";
    }

    @GetMapping("/dish/edit")
    public String showFormEditDish(@RequestParam int id, Model model, Principal principal, HttpSession httpSession) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        Dish dish = dishService.findById(id);
        List<DishType> listDishType = dishTypeService.findAll();
        model.addAttribute("listDishType", listDishType);
        model.addAttribute("obj", dish);
        dish.getPriceString();
        if (isDishOfCurrentRestaurant(user, id)) {
            model.addAttribute("obj", dish);
            model.addAttribute("userFullName", user.getFullname());
            return "views/restaurant/dish/edit_dish";
        } else return "redirect:/restaurant/dish";
    }

    @PostMapping("/dish/edit")
    public String processEditDish(@ModelAttribute("obj") Dish dish, @RequestParam("file") MultipartFile file, @RequestParam("currentUrlImage") String currentUrlImage, Model model, Principal principal, HttpSession httpSession) {
        try {
            User user = userDao.findByUsername(principal.getName());
            Restaurant restaurant = restaurantService.findByUserId(user.getId());
            dish.setRestaurant(restaurant);
            if (!file.isEmpty()) {
                Map uploadResult = cloudinary.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                String url = uploadResult.get("url").toString();
                dish.setImage(url);
            } else dish.setImage(currentUrlImage);
            dishService.save(dish);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/restaurant/dish";
    }

    @GetMapping("/dish/delete")
    public String deleteDish(@RequestParam int id, Model model, Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        Restaurant restaurant = restaurantService.findByUserId(user.getId());
        if (isDishOfCurrentRestaurant(user, id)) {
            try {
                Dish dish = dishService.findById(id);
                dishService.deleteById(id);
                String message = "Delete successfull !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", true);
            } catch (Exception e) {
                String message = "Delete failed !!!";
                model.addAttribute("message", message);
                model.addAttribute("isSuccess", false);
            }
        } else {
            String message = "Delete failed !!!";
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", false);
        }
        return "redirect:/restaurant/dish";
    }

    public boolean isDishOfCurrentRestaurant(User user, int id) {
        if (restaurantService.findByUserId(user.getId()).getId() == dishService.findById(id).getRestaurant().getId()) {
            return true;
        }
        return false;
    }

    @GetMapping("/order/create")
    public String createOrders(@RequestParam("listId") String listId, @RequestParam("bookingId") int bookingId){
        System.out.println("========================");
        System.out.println(listId);
        System.out.println("========================");
        String[] listDishId = listId.split(",");
        try {
            for (int i = 0; i < listDishId.length - 1; i++) {
                Booking booking = bookingService.findById(bookingId);
                Dish dish = dishService.findById(Integer.valueOf(listDishId[i]));
                Order order = orderService.findByBookingAndDish(booking, dish);
                if (order == null) {
                    order = new Order();
                    order.setBooking(booking);
                    order.setDish(dish);
                    order.setQuantity(1);
                    orderService.create(order);
                }
            }
            List<Order> listOrder = orderService.findByBooking(bookingService.findById(bookingId));
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }
}
