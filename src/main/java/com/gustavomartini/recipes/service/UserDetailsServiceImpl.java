package com.gustavomartini.recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gustavomartini.recipes.entity.User;
import com.gustavomartini.recipes.entity.UserDetailsImpl;
import com.gustavomartini.recipes.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User newUser) {
        User toSave = new User();
        toSave.setEmail(newUser.getEmail());
        toSave.setPassword(passwordEncoder.encode(newUser.getPassword()));
        toSave.setRole("ROLE_USER");
        return this.userRepository.save(toSave);
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.findUserByEmail(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(UserDetailsImpl::new).get();
    }
}
