package com.brisvegastech.registration.service;

import com.brisvegastech.registration.entity.UserEntity;
import com.brisvegastech.registration.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword()) // Must be encrypted in the DB
                .authorities(userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefixing role rules
                        .collect(Collectors.toList()))
                .build();
    }
}
}