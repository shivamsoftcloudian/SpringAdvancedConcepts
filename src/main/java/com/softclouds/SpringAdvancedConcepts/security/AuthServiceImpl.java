package com.softclouds.SpringAdvancedConcepts.security;

import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.exception.ElementAlreadyExistException;
import com.softclouds.SpringAdvancedConcepts.repository.UserRepository;
import com.softclouds.SpringAdvancedConcepts.security.payload.JwtResponseDTO;
import com.softclouds.SpringAdvancedConcepts.security.payload.LoginRequest;
import com.softclouds.SpringAdvancedConcepts.security.payload.RegisterRequest;
import com.softclouds.SpringAdvancedConcepts.security.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Override
    public JwtResponseDTO authenticate(LoginRequest request) throws AccountLockedException {

        User user = userRepository.findByUsername(request.getUsername());
        if (user != null && user.getLockExpiration() != null) {
            boolean isLockExpired = user.getLockExpiration().before(new Date());
            if (isLockExpired && user.isLocked()) {
                user.setLocked(false);
                user.setLockExpiration(null);
                userRepository.save(user);
                return login(request);
            }
            throw new AccountLockedException("Account is locked wait for " + user.getLockExpiration());
        } else if (user != null && !user.isLocked()) {
            return login(request);
        }
        throw new UsernameNotFoundException("User Not found with username: " + request.getUsername());
    }

    private JwtResponseDTO login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            log.debug("user is authenticated and generated security token");
            return new JwtResponseDTO(jwtService.generateToken(request.getUsername()));
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @Override
    public String register(RegisterRequest request) {
        User user = new User();
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ElementAlreadyExistException("username already exist..!!");
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setLocked(false);
        userRepository.save(user);
        log.debug("User registered successfully..!!");

        return "User registered successfully..!!";
    }
}
