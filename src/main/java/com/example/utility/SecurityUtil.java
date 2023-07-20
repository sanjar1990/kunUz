package com.example.utility;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exception.AppMethodNotAllowedException;
import com.example.exception.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;

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
        throw new AppMethodNotAllowedException("Method not allowed");
    }
    public static JwtDTO checkRoleForStaff(String jwt){
        JwtDTO jwtDTO=getJwtDTO(jwt);
        if(!jwtDTO.getRole().equals(ProfileRole.ADMIN)){
            return jwtDTO;
        }
        throw new AppMethodNotAllowedException("Method not allowed");
    }
    public static JwtDTO hasRole(HttpServletRequest request, ProfileRole... requiredRoles) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        if (requiredRoles == null) {
            return new JwtDTO(id, role);
        }
        boolean found = false;
        for (ProfileRole required : requiredRoles) {
            if (role.equals(required)) {
                found = true;
            }
        }
        if (!found) {
            throw new AppMethodNotAllowedException("");
        }
        return new JwtDTO(id, role);
    }
//    public static JwtDTO hasRole(HttpServletRequest request, ProfileRole... requiredRoles) {
//        Integer id= request.
//        JwtDTO jwtDTO = getJwtDTO(authToken);
//        if(requiredRoles==null){
//            return jwtDTO;
//        }
//        boolean found = false;
//        for (ProfileRole role : requiredRoles) {
//            if (jwtDTO.getRole().equals(role)) {
//                found = true;
//            }
//        }
//        if (!found) {
//            throw new AppMethodNotAllowedException("not found");
//        }
//        return jwtDTO;
//    }
}
