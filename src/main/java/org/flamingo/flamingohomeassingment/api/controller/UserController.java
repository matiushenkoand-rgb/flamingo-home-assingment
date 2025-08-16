package org.flamingo.flamingohomeassingment.api.controller;

import org.flamingo.flamingohomeassingment.api.model.User;
import org.flamingo.flamingohomeassingment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/byId")
    public User getUserById(@RequestParam(value = "id") Integer id){
        return userService.getAllUsers().stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.getAllUsers().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null));
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody Integer id) {
        userService.deleteUser(id);
    }

}