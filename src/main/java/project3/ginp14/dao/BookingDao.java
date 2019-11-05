package project3.ginp14.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project3.ginp14.entity.Booking;
import project3.ginp14.entity.Restaurant;

import java.util.List;

@Repository
public interface BookingDao extends CrudRepository<Booking, Integer> {
    Booking findByGuestNameOrGuestTelephone(String guestname, String telephone);
    List<Booking> findAllByRestaurant(Restaurant restaurant);
}
