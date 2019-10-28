package project3.ginp14.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project3.ginp14.entity.User;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameAndEmailAndTelephone(String username, String email, String telephone);
    List<User> findAll();
    User findById(int id);
}
