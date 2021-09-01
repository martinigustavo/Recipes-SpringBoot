package com.gustavomartini.recipes.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gustavomartini.recipes.entity.User;
import com.gustavomartini.recipes.service.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/register")
public class UserController {

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser) {
        Optional<User> user0 = this.userDetailsService.findUserByEmail(newUser.getEmail());

        if (user0.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(this.userDetailsService.save(newUser), HttpStatus.OK);
        }
    }
}

