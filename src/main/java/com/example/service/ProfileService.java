package com.example.service;

import com.example.dto.AttachDTO;
import com.example.dto.FilterProfileDTO;
import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotAvailable;
import com.example.exception.ItemNotFoundException;
import com.example.repository.AttachRepository;
import com.example.repository.CustomProfileRepository;
import com.example.repository.ProfileRepository;
import com.example.utility.CheckValidationUtility;
import com.example.utility.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CustomProfileRepository customProfileRepository;
    @Autowired
    private AttachRepository attachRepository;
    //Admin
    public ProfileDTO createProfile(ProfileDTO profileDTO, Integer adminId){
        //check
        ProfileDTO dto= checkValidationUtility.checkForStaff(profileDTO);
        // check is exist phone
        Boolean checkByPhone=profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(dto.getPhone(),ProfileStatus.ACTIVE);
        // check is exist email
        Boolean checkByEmail=profileRepository.existsAllByEmailAndVisibleTrueAndStatus(dto.getEmail(),ProfileStatus.ACTIVE);
        if(checkByPhone) throw new ItemAlreadyExists("this phone is exists!");
        if(checkByEmail) throw new ItemAlreadyExists("this email is exist");
        //to entity
        ProfileEntity profileEntity=toEntity(dto);
        profileEntity.setPrtId(adminId);
        profileRepository.save(profileEntity);
        profileDTO.setId(profileEntity.getId());
        return profileDTO;
    }
    //By Admin
    public String staffUpdateByAdmin(ProfileDTO profileDTO, Integer id){
        ProfileEntity profileEntity=getProfileEntity(id);
        //check validation
        if(profileDTO.getStatus()!=null){
            profileEntity.setStatus(profileDTO.getStatus());
        }
        if(profileDTO.getRole()!=null){
            profileEntity.setRole(profileDTO.getRole());
        }
        if(profileDTO.getPassword()!=null){
            profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        }
        if(profileDTO.getSurname()!=null){
            profileEntity.setSurname(profileDTO.getSurname());
        }
        if(profileDTO.getEmail()!=null){
            if(!profileDTO.getEmail().contains("@")){
                throw new AppBadRequestException("Enter valid email!");
            }
            if(profileRepository.existsAllByEmailAndVisibleTrueAndStatus(profileDTO.getEmail(),ProfileStatus.ACTIVE)){
                throw new AppBadRequestException("this email is exist");
            }
            profileEntity.setEmail(profileDTO.getEmail());
        }
        if(profileDTO.getPhone()!=null){
            if(!profileDTO.getPhone().startsWith("+998")){
                throw new AppBadRequestException("phone number should start with +998");
            } else if (profileDTO.getPhone().length()!=13) {
                throw new AppBadRequestException("phone length should be 13");
            }else if(!profileDTO.getPhone().substring(2).chars().allMatch(Character::isDigit)){
                throw new AppBadRequestException("invalid phone number");
            }
            if (profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(profileDTO.getPhone(),ProfileStatus.ACTIVE))
                throw new AppBadRequestException("this phone is exists!");
            profileEntity.setPhone(profileDTO.getPhone());
        }
        if(profileDTO.getName()!=null){
            profileEntity.setName(profileDTO.getName());
        }
        if(profileDTO.getPhotoId()!=null){
            profileEntity.setPhotoId(profileDTO.getPhotoId());
        }
        //save
        profileRepository.save(profileEntity);
        return "Updated";
    }

    //Staff

    public String updateProfile(ProfileDTO profileDTO, Integer id){
        ProfileEntity profileEntity=getProfileEntity(id);
        //profile status
        if(!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)){
            throw new ItemNotAvailable("Profile status is not active");
        }
        //check validation
        if(profileDTO.getPassword()!=null){
            profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        }
        if(profileDTO.getSurname()!=null){
            profileEntity.setSurname(profileDTO.getSurname());
        }
        if(profileDTO.getEmail()!=null){
            if(!profileDTO.getEmail().contains("@")){
                throw new AppBadRequestException("Enter valid email!");
            }
            if( profileRepository.existsAllByEmailAndVisibleTrueAndStatus(profileDTO.getEmail(),ProfileStatus.ACTIVE))
                throw new AppBadRequestException("this email is exist");
            profileEntity.setEmail(profileDTO.getEmail());
        }
        if(profileDTO.getPhone()!=null){
            if(!profileDTO.getPhone().startsWith("+998")){
                throw new AppBadRequestException("phone number should start with +998");
            } else if (profileDTO.getPhone().length()!=13) {
                throw new AppBadRequestException("phone length should be 13");
            }else if(!profileDTO.getPhone().substring(2).chars().allMatch(Character::isDigit)){
                throw new AppBadRequestException("invalid phone number");
            }
            if (profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(profileDTO.getPhone(),ProfileStatus.ACTIVE))
                throw new AppBadRequestException("this phone is exists!");
            profileEntity.setPhone(profileDTO.getPhone());
        }
        if(profileDTO.getName()!=null){
            profileEntity.setName(profileDTO.getName());
        }

        profileRepository.save(profileEntity);
        return "profile updated";
    }

    //6. Update Photo (ANY) (Murojat qilgan odamni rasmini upda qilish)
    public String updatePhoto(Integer profileId,String photoId){
        int n;
        ProfileEntity profileEntity=getProfileEntity(profileId);
        if(profileEntity.getPhotoId()!=null) {
            String url = profileEntity.getPhoto().getPath();
            String oldAttach=profileEntity.getPhoto().getId();
            n=profileRepository.updatePhoto(profileId,photoId);
            File file=new File(url);
            if(file.exists()){
                file.delete();
            }
            if(n>0){
                attachRepository.deleteById(oldAttach);
            }
            return "photo updated";
        }else {
            n=profileRepository.updatePhoto(profileId,photoId);
            return n>0?"photo updated":"photo not updated";
        }

    }

    //ByAdmin
    public PageImpl<ProfileDTO> profileListPagination(Integer page, Integer size){
        Sort sort=Sort.by(Sort.Direction.ASC,"createdDate");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<ProfileEntity>pageObj=profileRepository.findAllByVisibleTrue(pageable);
        List<ProfileDTO> dtoList=pageObj.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageObj.getPageable(),pageObj.getTotalElements());
    }

    //Admin
    public String deleteProfile(Integer id){
        return profileRepository.deleteVisible(id)>0?"deleted":"not deleted";
    }

    //7 Admin
    public PageImpl<ProfileDTO> filterPagination(FilterProfileDTO filterProfileDTO, Integer page, Integer size){
        FilterResultDTO<ProfileEntity> result=customProfileRepository.filterPagination(filterProfileDTO,page,size);
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,"createdDate"));
        List<ProfileDTO> dtoList=result.getContent().stream().map(s->toDto(s)).toList();
        dtoList.forEach(System.out::println);
        return new PageImpl<>(dtoList,pageable,result.getTotalElement());
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        ProfileEntity profileEntity=new ProfileEntity();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setEmail(profileDTO.getEmail());
        profileEntity.setPhone(profileDTO.getPhone());
        profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        profileEntity.setRole(profileDTO.getRole());
        profileEntity.setStatus(profileDTO.getStatus());
        return profileEntity;
    }
    public ProfileDTO toDto(ProfileEntity profileEntity){
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        profileDTO.setPhotoId(profileEntity.getPhotoId());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setPhone(profileEntity.getPhone());
        profileDTO.setPassword(profileEntity.getPassword());
        profileDTO.setRole(profileEntity.getRole());
        profileDTO.setStatus(profileEntity.getStatus());
        profileDTO.setVisible(profileEntity.getVisible());
        return profileDTO;
    }

    public ProfileEntity getProfileEntity(Integer profileId){
        return profileRepository.findByIdAndVisibleTrue(profileId)
                .orElseThrow(()-> new ItemNotFoundException("profile not found"));
    }

}
