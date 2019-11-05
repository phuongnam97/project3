package project3.ginp14.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.ginp14.dao.BookingDao;
import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Restaurant;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingDao bookingDao;

    @Override
    public void save(Booking booking) {
        bookingDao.save(booking);
    }

    @Override
    public List<Booking> findAllByRestaurantId(Restaurant restaurant) {
        return bookingDao.findAllByRestaurant(restaurant);
    }

}
