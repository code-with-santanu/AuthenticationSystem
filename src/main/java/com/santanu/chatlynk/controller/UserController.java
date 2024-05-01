package com.santanu.gossipZ.controller;


import com.santanu.gossipZ.entity.UserEntity;
import com.santanu.gossipZ.model.User;
import com.santanu.gossipZ.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    // Define User Service object
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // expose "/employees" endpoint to return list of employees
    @GetMapping("/users")
    public List<User> getAllUsers()
    {
        return userService.getAllUser();
    }

    // expose endpoint to return single Employee
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId)
    {
        User user = userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    // expose endpoint to add an employee
    @PostMapping("/users")
    public User addUser(@RequestBody User user)
    {
        return userService.addUser(user);
    }

    // expose endpoint to update an employee
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user)
    {
        user= userService.updateUser(userId,user);

        return ResponseEntity.ok(user);
    }

    //expose endpoint to delete an employee
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String,Boolean>> deleteUser(@PathVariable Long userId)
    {

        boolean deleted = false;
        deleted = userService.deleteUser(userId);

        Map<String,Boolean> response = new HashMap<>();
        response.put("Deleted",true);

        return ResponseEntity.ok(response);

    }
}
