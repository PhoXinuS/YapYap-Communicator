package com.example.texttuah.service;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.ChatUserDetails;
import com.example.texttuah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChatUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public ChatUserDetailsService(UserService userRepository) {
        this.userService = userRepository;
    }

    // email is the username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ChatUser user = userService.findByEmail(email);
        if (user == null) {
            System.out.println("User not found!");
            throw new UsernameNotFoundException("user not found");
        }

        return new ChatUserDetails(user);
    }
}
