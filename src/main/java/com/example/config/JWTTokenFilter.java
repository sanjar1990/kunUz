package com.example.config;

import com.example.dto.JwtDTO;
import com.example.exception.UnAuthorizedException;
import com.example.utility.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
   @Autowired
   private UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher=new AntPathMatcher();
        return Arrays
                .stream(SpringSecurityConfig.AUTH_WHITELIST)
                .anyMatch(p->pathMatcher.match(p,request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request,response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("message","token not found");
            return;
        }
        String token=authHeader.substring(7);
        JwtDTO jwtDTO;
        try {
            jwtDTO= JWTUtil.decode(token);
        }catch (UnAuthorizedException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("message",e.getMessage());
            return;
        }
        UserDetails userDetails=userDetailsService.loadUserByUsername(jwtDTO.getPhone());
        UsernamePasswordAuthenticationToken
                authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);


    }
}
