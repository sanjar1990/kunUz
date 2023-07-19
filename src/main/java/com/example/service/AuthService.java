package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exception.ItemAlreadyExists;
import com.example.repository.ProfileRepository;
import com.example.utility.CheckValidationUtility;
import com.example.utility.JWTUtil;
import com.example.utility.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;

    public ApiResponseDTO login(AuthDTO authDTO) {
        //check
        checkValidationUtility.checkForLogin(authDTO);
        Optional<ProfileEntity> optional=profileRepository
                .findAllByPhoneAndPasswordAndVisibleTrue(authDTO.getPhone(), MD5Util.encode(authDTO.getPassword()));
        if(optional.isEmpty()) return new ApiResponseDTO(false,"Profile not found");
        ProfileEntity profileEntity=optional.get();
        if(!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)){
            return new ApiResponseDTO(false,"Profile not active");
        }
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setPhone(profileDTO.getPhone());
        profileDTO.setRole(profileEntity.getRole());
        profileDTO.setJwt(JWTUtil.encode(profileEntity.getId(),profileEntity.getRole()));
        return new ApiResponseDTO(true, profileDTO);
    }
    //register user
    public ProfileDTO registration(ProfileDTO dto){
        //check
        checkValidationUtility.checkForUser(dto);
        // check is exist phone
        Boolean checkByPhone=profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(dto.getPhone(),ProfileStatus.ACTIVE);
        // check is exist email
        Boolean checkByEmail=profileRepository.existsAllByEmailAndVisibleTrueAndStatus(dto.getEmail(),ProfileStatus.ACTIVE);
        if(checkByPhone) throw new ItemAlreadyExists("this phone is exists!");
        if(checkByEmail) throw new ItemAlreadyExists("this email is exist");
        ProfileEntity profileEntity =toEntity(dto);
        profileEntity.setRole(ProfileRole.USER);
        profileRepository.save(profileEntity);
        dto.setId(profileEntity.getId());
        dto.setStatus(profileEntity.getStatus());
        dto.setRole(profileEntity.getRole());
        return dto;
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        ProfileEntity profileEntity=new ProfileEntity();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setEmail(profileDTO.getEmail());
        profileEntity.setPhone(profileDTO.getPhone());
        profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        return profileEntity;
    }
}
