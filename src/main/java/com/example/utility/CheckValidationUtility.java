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
