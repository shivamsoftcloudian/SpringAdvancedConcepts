package com.softclouds.SpringAdvancedConcepts.security.util;

import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("User not found with username {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserDetailsImpl(user);
    }
}
