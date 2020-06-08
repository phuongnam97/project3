package project3.ginp14.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.Table;

import java.util.List;

@Repository
public interface TableDao extends CrudRepository<Table, Integer> {
    List<Table> findByRestaurant(Restaurant restaurant);
    @Query(value = "select * from tables where code like ?1 " +
            "order by code desc " +
            "limit 1", nativeQuery = true)
    Table findTopByCodeContainsOrderByCodeDesc(String prefix);

    @Query(value = "", nativeQuery = true)
    List<Table> findTableEmptyByRestaurantAndByBookingTime(Restaurant restaurant, String bookingTime, @Value("${meal.time}") int mealTime);

    Table findById(int id);
}
