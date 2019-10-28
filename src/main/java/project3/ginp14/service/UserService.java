package project3.ginp14.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project3.ginp14.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User findUserbyUsername(String username);
    public boolean checkExistInfo(String username, String email, String telephone);
    public void save(User user);
    public List<User> findAll();
    public void blockUser(int id);
    public void unblockUser(int id);
}
