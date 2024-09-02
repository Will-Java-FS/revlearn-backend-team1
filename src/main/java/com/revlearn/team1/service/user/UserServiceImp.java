package com.revlearn.team1.service.user;

import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean checkExisting(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private void checkRole(String role) {
        Set<String> roles = Set.of("Student", "Educator", "Institution");
        if (!roles.contains(role)) {
            throw new RuntimeException("Invalid Role!");
        }
    }

    public User createUser(User user) {
        if (checkExisting(user.getUsername())) {
            throw new RuntimeException("Username Existing. Please try other username.");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("Student");
        } else {
            checkRole(user.getRole());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username."));
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

//    @Override
//    public User save(User user) {
//        return userRepository.save(user);
//    }
}

