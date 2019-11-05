package project3.ginp14.service;

import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Restaurant;

import java.util.List;

public interface BookingService {
    void save(Booking booking);
    List<Booking> findAllByRestaurantId(Restaurant restaurant);
}
