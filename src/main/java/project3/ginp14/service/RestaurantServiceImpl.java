package project3.ginp14.service;

import org.springframework.stereotype.Service;
import project3.ginp14.dao.RestaurantDao;
import project3.ginp14.entity.Restaurant;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    private RestaurantDao restaurantDao;

    @Override
    public Restaurant findById(int id) {
        return restaurantDao.findById(id);
    }
}
