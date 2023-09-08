package com.example.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CheckValidationUtilityTest {
    CheckValidationUtility checkValidationUtility;
   @BeforeEach
    public void setup(){
        checkValidationUtility= new CheckValidationUtility();
    }
    @Test
    public void itShouldValidatePhone(){
        //given
        String phone="+998908070176";
        //when
        boolean result= checkValidationUtility.checkForPhone(phone);
        //then
        Assertions.assertTrue(result);
    }
    @Test
    public void itShouldValidateWrongSizePhone(){
        //given
        String phone="+99890807017690";
        //when
        boolean result= checkValidationUtility.checkForPhone(phone);
        //then
        Assertions.assertFalse(result,"uzunligi xatto");
    }
    @Test
    public void itShouldValidateWrongPhoneNoPlus(){
        //given
        String phone="998908070176";
        //when
        boolean result= checkValidationUtility.checkForPhone(phone);
        //then
        Assertions.assertFalse(result,"plus bor xatto");
    }
    @Disabled
    @Test
    public void itShouldValidateWrongPhoneNull(){
        //given
        String phone="998908070176";
        //when
        boolean result= checkValidationUtility.checkForPhone(null);
        //then
        Assertions.assertFalse(result,"plus bor xatto");
    }
    @ParameterizedTest
    @CsvSource({"998908070176",".","null"})
    public void itShouldValidateWrongPhoneAll(String phone){
        //given

        //when
        boolean result= checkValidationUtility.checkForPhone(phone);
        //then
        Assertions.assertFalse(result,"plus bor xatto");
    }

}
