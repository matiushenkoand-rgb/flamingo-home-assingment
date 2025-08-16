package org.flamingo.flamingohomeassingment.service;

import org.flamingo.flamingohomeassingment.api.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static List<User> userList;

    public UserService() {
        userList = new ArrayList<>();

        userList.add(new User(1, "Thor", "Odinson", "Strongest Avenger"));
        userList.add(new User(2, "Tony", "Stark", "Dr. Doom"));
        userList.add(new User(3, "Bruce", "Banned", "Hulk Smash"));
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void updateUser(User user) {
        for (User userFromList : userList) {
            if (Objects.equals(userFromList.getId(), user.getId())) {
                userFromList.setFirstName(user.getFirstName());
                userFromList.setLastName(user.getLastName());
                userFromList.setUserName(user.getUserName());
            }
        }
    }

    public void deleteUser(Integer id) {
        userList.removeIf(userFromList -> Objects.equals(userFromList.getId(), id));
    }

    public static List<User> getUsers() {
        return userList;
    }
}
