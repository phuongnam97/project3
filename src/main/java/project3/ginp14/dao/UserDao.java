package project3.ginp14.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.ginp14.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameAndEmailAndTelephone(String username, String email, String telephone);
    List<User> findAll();
    User findById(int id);
}
