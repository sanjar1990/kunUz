package com.example.service;

import com.example.dto.*;
import com.example.entity.ProfileEntity;
import com.example.entity.SmsHistoryEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.enums.SmsStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.repository.SmsHistoryRepository;
import com.example.utility.CheckValidationUtility;
import com.example.utility.JWTUtil;
import com.example.utility.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;


    public ApiResponseDTO login(AuthDTO authDTO) {
        //check
        checkValidationUtility.checkForPhone(authDTO.getPhone());
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
        profileDTO.setJwt(JWTUtil.encode(profileEntity.getPhone(),profileEntity.getRole()));
        return new ApiResponseDTO(true, profileDTO);
    }
    //register user
    public ApiResponseDTO registrationByEmail(RegistrationDTO dto){
        //check
        checkValidationUtility.checkForPhone(dto.getPhone());
        // check is exist phone
        Boolean checkByPhone=profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(dto.getPhone(),ProfileStatus.ACTIVE);
        if(checkByPhone)return new ApiResponseDTO(false,"this phone is exists!");
        // check is exist email
       Optional<ProfileEntity> optional=profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
       if(optional.isPresent() && optional.get().getStatus().equals(ProfileStatus.ACTIVE)){
           return new ApiResponseDTO(false,"this email is exists!");
       }else if(optional.isPresent() && optional.get().getStatus().equals(ProfileStatus.REGISTRATION)){
           profileRepository.deleteById(optional.get().getId());
       }
        ProfileEntity profileEntity =toEntity(dto);
        profileEntity.setRole(ProfileRole.ROLE_USER);
        profileEntity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(profileEntity);
        mailSenderService.sendEmailVerification(profileEntity);//send registration verification
        return new ApiResponseDTO(true,"The verification link was send to your email");
    }
    //registration by phone
    public ApiResponseDTO registrationByPhone(RegistrationDTO dto) {
        //check phone
        checkValidationUtility.checkForPhone(dto.getPhone());
        // check is exist email
        Optional<ProfileEntity> optional=profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent() && optional.get().getStatus().equals(ProfileStatus.ACTIVE)){
            return new ApiResponseDTO(false,"this email is exists!");
        }
        // check is exist phone
       Optional<ProfileEntity>checkByPhone=profileRepository
               .findByPhoneAndVisibleTrue(dto.getPhone());
        if (checkByPhone.isPresent()){
            ProfileEntity entity=checkByPhone.get();
            if(entity.getStatus().equals(ProfileStatus.ACTIVE) ||
                    entity.getStatus().equals(ProfileStatus.BLOCKED)){
                return new ApiResponseDTO(false,"this phone is exists!");
            }
            if(entity.getStatus().equals(ProfileStatus.REGISTRATION)
                    && entity.getCreatedDate().plusMinutes(2).isAfter(LocalDateTime.now())){
                return new ApiResponseDTO(false,"try after 2 minutes");
            } else if (entity.getStatus().equals(ProfileStatus.REGISTRATION)
                    && entity.getCreatedDate().plusMinutes(2).isBefore(LocalDateTime.now())) {
                profileRepository.deleteById(entity.getId());
            }
        }

        ProfileEntity profileEntity =toEntity(dto);
        profileEntity.setRole(ProfileRole.ROLE_USER);
        profileEntity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(profileEntity);
        SmsDTO smsDTO= smsSenderService.SendRegistrationSms(profileEntity);
        return new ApiResponseDTO(true,smsDTO);




    }
    public ApiResponseDTO emailVerification(String jwt) {
        JwtDTO jwtDTO=JWTUtil.decodeEmailJWT(jwt);
        Optional<ProfileEntity> optional=profileRepository.findById(jwtDTO.getId());
        if(optional.isEmpty()) throw new ItemNotFoundException("Profile not found");
        ProfileEntity entity=optional.get();
        if(!entity.getStatus().equals(ProfileStatus.REGISTRATION)){
            throw new AppBadRequestException("verification link is expired!");
        }
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return new ApiResponseDTO(true,"you have been successfully verified!");
    }

    public ProfileEntity toEntity(RegistrationDTO dto){
        ProfileEntity profileEntity=new ProfileEntity();
        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
        profileEntity.setEmail(dto.getEmail());
        profileEntity.setPhone(dto.getPhone());
        profileEntity.setPassword(MD5Util.encode(dto.getPassword()));
        return profileEntity;
    }
    public String phoneVerification(String phone, String message) {
        System.out.println(phone+"  "+message);
        Optional<SmsHistoryEntity>optional=smsHistoryRepository
                .findByPhoneAndMessageOrderByCreatedDateDesc(phone,message);
        if(optional.isEmpty()){
            throw new AppBadRequestException("Verification is failed!");
        }
        SmsHistoryEntity entity=optional.get();
        if(entity.getStatus().equals(SmsStatus.USED)){
            throw new AppBadRequestException("Sms code is not valid");
        }
        if(entity.getCreatedDate().plusMinutes(2).isBefore(LocalDateTime.now())){
            throw new AppBadRequestException("Sms code is expired");
        }
        int n=profileRepository.updateStatusWithPhone(ProfileStatus.ACTIVE,phone);
         int m=smsHistoryRepository.updateSmsHistoryStatus(SmsStatus.USED, phone);
        return n!=0?"your account is activated":"Status not activated";
    }


}
