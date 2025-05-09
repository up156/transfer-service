package com.transfer.demo.config.security;


import com.transfer.demo.model.User;
import com.transfer.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {

        User user = userRepository.findById(Long.parseLong(userIdStr))
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id"));

        return new JwtUser(
                user.getId(),
                null,
                null,
                List.of()
        );
    }
}