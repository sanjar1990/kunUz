package com.example.utility;

import com.example.dto.*;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CheckValidationUtility {
       public ProfileDTO checkForStaff(ProfileDTO profileDTO){
        if(profileDTO.getName()==null || profileDTO.getName().isBlank()
        || profileDTO.getName().length()<3){
            throw new AppBadRequestException("name not found!");
        }
        if (profileDTO.getSurname()==null || profileDTO.getSurname().isBlank()
        || profileDTO.getSurname().length()<3){
            throw new AppBadRequestException("surname not found!");
        }
        if(profileDTO.getPhone()==null || profileDTO.getPhone().isBlank()){
            throw new AppBadRequestException("phone not found");
        } else if(!profileDTO.getPhone().startsWith("+998")){
            throw new AppBadRequestException("phone number should start with +998");
        } else if (profileDTO.getPhone().length()!=13) {
            throw new AppBadRequestException("phone length should be 13");
        }else if(!profileDTO.getPhone().substring(2).chars().allMatch(Character::isDigit)){
            throw new AppBadRequestException("invalid phone number");
        }
        if(profileDTO.getEmail()==null || profileDTO.getEmail().isBlank()){
            throw new AppBadRequestException("email not found");
        }
        if(!profileDTO.getEmail().contains("@")){
            throw new AppBadRequestException("Enter valid email!");
        }
        if(profileDTO.getPassword()==null || profileDTO.getPassword().isBlank()
        || profileDTO.getPassword().length()<5){
            throw new AppBadRequestException("password not found");
        }
        if(profileDTO.getRole()==null){
            throw new AppBadRequestException("Role not found!");
        }
        if(profileDTO.getStatus()==null){
            profileDTO.setStatus(ProfileStatus.ACTIVE);
        }
        return profileDTO;
    }


    public void checkRegion(RegionDTO regionDTO) {
           if (regionDTO.getNameEn()==null || regionDTO.getNameEn().isBlank()){
               throw new AppBadRequestException("Region english name not found!");
           }
        if (regionDTO.getNameRu()==null || regionDTO.getNameRu().isBlank()){
            throw new AppBadRequestException("Region russian name not found!");
        }
        if (regionDTO.getNameUz()==null || regionDTO.getNameUz().isBlank()){
            throw new AppBadRequestException("Region uzbek name not found!");
        }
        if(regionDTO.getOrderNumber()==null){
            throw new AppBadRequestException("Region order number not found!");
        }
    }

    public void checkForPhone(String phone) {
         if(!phone.startsWith("+998")){
            throw new AppBadRequestException("phone number should start with +998");
        } else if (phone.length()!=13) {
            throw new AppBadRequestException("phone length should be 13");
        }else if(!phone.substring(2).chars().allMatch(Character::isDigit)){
            throw new AppBadRequestException("invalid phone number");
        }
    }

}
