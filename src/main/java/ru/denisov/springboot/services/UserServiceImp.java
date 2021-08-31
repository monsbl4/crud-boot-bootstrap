package ru.denisov.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.denisov.springboot.dao.RoleDao;
import ru.denisov.springboot.dao.UserDao;
import ru.denisov.springboot.models.User;

import java.util.Collections;
import java.util.List;


@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public List<User> index() {
        return userDao.index();
    }

    @Override
    public User show(int id) {
        return userDao.show(id);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public void update( User updatedUser) {
        updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        userDao.update(updatedUser);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User 'username' not found!", username));
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getAuthorities());
        return userDetails;
    }

    public boolean saveUser(User user) {
        User userFromDB = getUserByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        save(user);
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
}
