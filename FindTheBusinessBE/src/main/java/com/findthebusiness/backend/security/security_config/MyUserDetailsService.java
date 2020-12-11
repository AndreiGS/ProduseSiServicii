package com.findthebusiness.backend.security.security_config;

import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findUsersById(s);

        user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.map(MyUserDetails::new).get();
    }
}
