package project3.ginp14.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.ginp14.dao.OrderDao;
import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Dish;
import project3.ginp14.entity.Order;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> findByBooking(Booking booking) {
        List<Order> listOrder = orderDao.findByBooking(booking);
        return listOrder;
    }

    @Override
    public Order findByBookingAndDish(Booking booking, Dish dish) {
        Order order = orderDao.findByBookingAndDish(booking, dish);
        return order;
    }

    @Override
    public void create(Order order) {
        orderDao.save(order);
    }
}
