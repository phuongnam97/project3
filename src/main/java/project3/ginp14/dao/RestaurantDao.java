package project3.ginp14.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.RestaurantType;
import project3.ginp14.entity.User;

@Repository
public interface RestaurantDao extends CrudRepository<Restaurant, Integer> {
    Restaurant findById(int id);

    @Query(value = "SELECT r.* FROM restaurants r where r.user_id = ?1", nativeQuery = true)
    Restaurant findByUserId(Integer id);
}
