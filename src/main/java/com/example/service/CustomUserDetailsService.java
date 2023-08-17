package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.entity.ProfileEntity;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional=profileRepository.findByPhoneAndVisibleTrue(username);
        if(optional.isEmpty()) throw new ItemNotFoundException("invalid login or password!");
        return new CustomUserDetails(optional.get());
    }

}
