package project3.ginp14.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.ginp14.dao.RestaurantDao;
import project3.ginp14.entity.Restaurant;
import project3.ginp14.entity.User;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private RestaurantDao restaurantDao;

    @Override
    public Restaurant findById(int id) {
        return restaurantDao.findById(id);
    }

    @Override
    public Restaurant findByUserId(int id) {
        return restaurantDao.findByUserId(id);
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurantDao.save(restaurant);
    }
}
