package com.example.config;

import com.example.dto.JwtDTO;
import com.example.exception.UnAuthorizedException;
import com.example.utility.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Configuration
public class JWTFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        final String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer")){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("message","Token not found");
        return;
        }
        String tooken=authHeader.substring(7);
        JwtDTO jwtDTO;
        try {
            jwtDTO= JWTUtil.decode(tooken);
        }catch (UnAuthorizedException | JwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("message",e.getMessage());
            return;
        }
        request.setAttribute("id",jwtDTO.getId());
        request.setAttribute("role",jwtDTO.getRole());
        filterChain.doFilter(request,response);
    }
}
