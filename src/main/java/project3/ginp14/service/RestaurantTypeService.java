package project3.ginp14.service;

import project3.ginp14.entity.RestaurantType;

import java.util.List;

public interface RestaurantTypeService {
    public List<RestaurantType> findAll();
    public void save(RestaurantType restaurantType);
    public RestaurantType findById(int id);
    public void deleteById(int id);
}
