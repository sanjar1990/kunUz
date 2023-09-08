package com.example.utility;

import com.example.dto.*;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CheckValidationUtility {
//    public void checkForPhone2(String phone) {
//         if(!phone.startsWith("+998")){
//            throw new AppBadRequestException("phone number should start with +998");
//        } else if (phone.length()!=13) {
//            throw new AppBadRequestException("phone length should be 13");
//        }else if(!phone.substring(2).chars().allMatch(Character::isDigit)){
//            throw new AppBadRequestException("invalid phone number");
//        }
//    }

    //    @Override
    public Boolean checkForPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        Pattern p = Pattern.compile("[+]998[0-9]{9}");//. represents single character
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
