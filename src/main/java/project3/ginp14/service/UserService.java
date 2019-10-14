package project3.ginp14.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project3.ginp14.entity.User;

public interface UserService extends UserDetailsService {
    User findUserbyUsername(String username);
    boolean checkExistInfo(String username, String email, String telephone);
    public void save(User user);
}
