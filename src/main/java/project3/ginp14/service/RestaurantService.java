package project3.ginp14.service;

import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.User;

public interface RestaurantService {
    Restaurant findById(int id);
    Restaurant findByUserId(int id);
    void save(Restaurant restaurant);
}
