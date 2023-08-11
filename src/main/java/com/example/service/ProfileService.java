package com.example.service;

import com.example.dto.*;
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
         checkValidationUtility.checkForPhone(profileDTO.getPhone());
        // check is exist phone
        Boolean checkByPhone=profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(profileDTO.getPhone(),ProfileStatus.ACTIVE);
        // check is exist email
        Boolean checkByEmail=profileRepository.existsAllByEmailAndVisibleTrueAndStatus(profileDTO.getEmail(),ProfileStatus.ACTIVE);
        if(checkByPhone) throw new ItemAlreadyExists("this phone is exists!");
        if(checkByEmail) throw new ItemAlreadyExists("this email is exist");
        //to entity
        ProfileEntity profileEntity=toEntity(profileDTO);
        profileEntity.setPrtId(adminId);
        profileRepository.save(profileEntity);
        profileDTO.setId(profileEntity.getId());
        return profileDTO;
    }
    //By Admin
    public String staffUpdateByAdmin(ProfileDTO profileDTO, Integer id){
        ProfileEntity profileEntity=getProfileEntity(id);
            profileEntity.setStatus(profileDTO.getStatus());
            profileEntity.setRole(profileDTO.getRole());
            profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
            profileEntity.setSurname(profileDTO.getSurname());
            checkValidationUtility.checkForPhone(profileDTO.getPhone());
            if (profileRepository.existsAllByPhoneAndVisibleTrueAndStatus(profileDTO.getPhone(),ProfileStatus.ACTIVE))
                throw new AppBadRequestException("this phone is exists!");
            profileEntity.setPhone(profileDTO.getPhone());
            profileEntity.setName(profileDTO.getName());
            profileEntity.setPhotoId(profileDTO.getPhotoId());
            profileEntity.setEmail(profileDTO.getEmail());
        //save
        profileRepository.save(profileEntity);
        return "Updated";
    }

    //Staff

    public String updateProfile(ProfileUpdateDetailDTO dto, Integer id){
        ProfileEntity profileEntity=getProfileEntity(id);
        //profile status
        if(!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)){
            throw new ItemNotAvailable("Profile status is not active");
        }
        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
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
