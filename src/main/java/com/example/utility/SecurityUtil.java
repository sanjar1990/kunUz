package com.example.utility;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exception.MethodNotAllowedException;
import com.example.exception.UnAuthorizedException;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String authToken){
        if(authToken.startsWith("Bearer")){
            String jwt=authToken.substring(7);
            return JWTUtil.decode(jwt);
        }
        throw new UnAuthorizedException("Not Authorized");
    }
    public static JwtDTO checkRoleForAdmin(String jwt, ProfileRole profileRole){
        JwtDTO jwtDTO=getJwtDTO(jwt);
        if(jwtDTO.getRole().equals(profileRole)){
            return jwtDTO;
        }
        throw new MethodNotAllowedException("Method not allowed");
    }
    public static JwtDTO checkRoleForStaff(String jwt){
        JwtDTO jwtDTO=getJwtDTO(jwt);
        if(!jwtDTO.getRole().equals(ProfileRole.ADMIN)){
            return jwtDTO;
        }
        throw new MethodNotAllowedException("Method not allowed");
    }
}
