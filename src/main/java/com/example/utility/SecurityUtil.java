package com.example.utility;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exception.MethodNotAllowedException;
import com.example.exception.UnAuthorizedException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String authToken){
        if(authToken.startsWith("Bearer")){
            String jwt=authToken.substring(7);
            return JWTUtil.decode(jwt);
        }
        throw new UnAuthorizedException("Not Authorized");
    }
    public static JwtDTO hasRole(HttpServletRequest request, ProfileRole... requiredRole){
        Integer id=(Integer) request.getAttribute("id");
        ProfileRole role=(ProfileRole)request.getAttribute("role");
        if(requiredRole==null){
            return new JwtDTO(id, role);
        }
        boolean found=false;
        for (ProfileRole r: requiredRole){
            if(role.equals(r)){
                found=true;
            }
        }
        if(!found){
            throw new MethodNotAllowedException("Method not allowed");
        }
        return new JwtDTO(id, role);
    }
    public static JwtDTO hasRole(String jwt, ProfileRole... requiredRole){
        JwtDTO jwtDTO=getJwtDTO(jwt);
        if(requiredRole==null){
        return jwtDTO;
        }
        boolean found=false;
        for (ProfileRole r: requiredRole) {
            if(jwtDTO.getRole().equals(r)){
                found=true;
            }
        }
        if(!found){
            throw new MethodNotAllowedException("Method not allowed");
        }
        return jwtDTO;
    }

}
