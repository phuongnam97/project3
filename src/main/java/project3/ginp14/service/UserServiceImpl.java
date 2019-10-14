package project3.ginp14.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project3.ginp14.dao.UserDao;
import project3.ginp14.entity.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public User findUserbyUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean checkExistInfo(String username, String email, String telephone) {
        User user = userDao.findByUsernameAndEmailAndTelephone(username, email, telephone);
        if (user == null){
            return false;
        }
        return true;
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null){ throw new UsernameNotFoundException("Username not found");}
        return user;
    }
}
