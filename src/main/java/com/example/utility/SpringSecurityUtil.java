package com.example.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SpringSecurityUtil {
    public static String getCurrentUserName(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName=authentication.getName();
        System.out.println(currentPrincipalName);
        return currentPrincipalName;
    }
    public static UserDetails getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }
}


