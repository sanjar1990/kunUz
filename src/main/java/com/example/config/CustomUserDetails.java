package com.example.config;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final ProfileEntity profileEntity;

    public CustomUserDetails(ProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority>list=new LinkedList<>();
        list.add(new SimpleGrantedAuthority(profileEntity.getRole().name()));
        return list;
    }

    @Override
    public String getPassword() {
        return profileEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return profileEntity.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return profileEntity.getVisible();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return profileEntity.getStatus().equals(ProfileStatus.ACTIVE);
    }

    public ProfileEntity getProfileEntity() {
        return profileEntity;
    }
}
