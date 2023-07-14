package com.example.service;

import com.example.dto.FilterProfileDTO;
import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotAvailable;
import com.example.exception.ItemNotFoundException;
import com.example.repository.CustomProfileRepository;
import com.example.repository.ProfileRepository;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
    //Admin
    public ProfileDTO createProfile(ProfileDTO profileDTO){
        //check
        ProfileDTO dto= checkValidationUtility.checkForStaff(profileDTO);
        // check is exist phone
        Optional<ProfileEntity> getByPhone=profileRepository.findAllByPhone(dto.getPhone());
        // check is exist email
        Optional<ProfileEntity> getByEmail=profileRepository.findAllByEmail(dto.getEmail());
        if(getByPhone.isPresent()) throw new ItemAlreadyExists("this phone is exists!");
        if(getByEmail.isPresent()) throw new ItemAlreadyExists("this email is exist");
        //to entity
        ProfileEntity profileEntity=toEntity(dto);
        profileRepository.save(profileEntity);
        profileDTO.setId(profileEntity.getId());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        profileDTO.setVisible(profileEntity.getVisible());
        return profileDTO;
    }
    //By Admin
    public String staffUpdateByAdmin(ProfileDTO profileDTO, Integer id){
        //check is exists
        Optional<ProfileEntity> optional=profileRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("profile not found");
        ProfileEntity profileEntity=optional.get();
        //check validation
        if(profileDTO.getStatus()!=null){
            profileEntity.setStatus(profileDTO.getStatus());
        }
        if(profileDTO.getRole()!=null){
            profileEntity.setRole(profileDTO.getRole());
        }
        if(profileDTO.getPassword()!=null){
            profileEntity.setPassword(profileDTO.getPassword());
        }
        if(profileDTO.getSurname()!=null){
            profileEntity.setSurname(profileDTO.getSurname());
        }
        if(profileDTO.getEmail()!=null){
            if(!profileDTO.getEmail().contains("@")){
                throw new AppBadRequestException("Enter valid email!");
            }
            Optional<ProfileEntity> getByEmail=profileRepository.findAllByEmail(profileDTO.getEmail());
            if(getByEmail.isPresent()) throw new AppBadRequestException("this email is exist");
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
            Optional<ProfileEntity> entityOptional=profileRepository.findAllByPhone(profileDTO.getPhone());
            if (entityOptional.isPresent()) throw new AppBadRequestException("this phone is exists!");
            profileEntity.setPhone(profileDTO.getPhone());
        }
        if(profileDTO.getName()!=null){
            profileEntity.setName(profileDTO.getName());
        }
        if(profileDTO.getVisible()!=null){
            profileEntity.setVisible(profileDTO.getVisible());
        }
        if(profileDTO.getCreatedDate()!=null){
            profileEntity.setCreatedDate(profileDTO.getCreatedDate());
        }
        if(profileDTO.getPhotoId()!=null){
            profileEntity.setPhotoId(profileDTO.getPhotoId());
        }
        //save
        profileRepository.save(profileEntity);
        return "Updated";
    }

    //Staff

    public String updateStaffByStaff(ProfileDTO profileDTO, Integer id){
        //check is exists
        Optional<ProfileEntity> optional=profileRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("profile not found");
        ProfileEntity profileEntity=optional.get();
        //profile status and visible
        if(!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)){
            throw new ItemNotAvailable("Profile status is not active");
        }
        if(!profileEntity.getVisible()){
            throw new ItemNotAvailable("Profile not available");
        }
        //check validation
        if(profileDTO.getPassword()!=null){
            profileEntity.setPassword(profileDTO.getPassword());
        }
        if(profileDTO.getSurname()!=null){
            profileEntity.setSurname(profileDTO.getSurname());
        }
        if(profileDTO.getEmail()!=null){
            if(!profileDTO.getEmail().contains("@")){
                throw new AppBadRequestException("Enter valid email!");
            }
            Optional<ProfileEntity> getByEmail=profileRepository.findAllByEmail(profileDTO.getEmail());
            if(getByEmail.isPresent()) throw new AppBadRequestException("this email is exist");
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
            Optional<ProfileEntity> entityOptional=profileRepository.findAllByPhone(profileDTO.getPhone());
            if (entityOptional.isPresent()) throw new AppBadRequestException("this phone is exists!");
            profileEntity.setPhone(profileDTO.getPhone());
        }
        if(profileDTO.getName()!=null){
            profileEntity.setName(profileDTO.getName());
        }
        profileRepository.save(profileEntity);
        return "profile updated";
    }

    //ByAdmin
    public PageImpl<ProfileDTO> profileListPagination(Integer page, Integer size){
        Sort sort=Sort.by(Sort.Direction.ASC,"createdDate");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<ProfileEntity>pageObj=profileRepository.findAll(pageable);
        List<ProfileDTO> dtoList=pageObj.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageObj.getPageable(),pageObj.getTotalElements());
    }

    //Admin
    public String deleteProfile(Integer id){
        Optional<ProfileEntity> optional=profileRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("profile not found");
        ProfileEntity profileEntity=optional.get();
        if(!profileEntity.getVisible()){
            throw new ItemNotAvailable("Profile not available");
        }
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
        profileEntity.setPassword(profileDTO.getPassword());
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

}
