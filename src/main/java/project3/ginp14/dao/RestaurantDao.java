package project3.ginp14.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.RestaurantType;

@Repository
public interface RestaurantDao extends CrudRepository<RestaurantType, Integer> {
    Restaurant findById(int id);
}
