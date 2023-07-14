package com.example.service;

import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.RegionLanguageMapper;
import com.example.repository.RegionRepository;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    //1 create By Admin
    public RegionDTO createRegion(RegionDTO regionDTO){
        checkValidationUtility.checkRegion(regionDTO);
        Optional<RegionEntity> optional=regionRepository
                .findByNameEngOrNameUzOrNameRu(regionDTO.getNameEng(),regionDTO.getNameRu(),regionDTO.getNameUz());
        if (optional.isPresent()) throw new ItemAlreadyExists("This Region already exists");
        Optional<RegionEntity>byOrderNum=regionRepository.findByOrderNumber(regionDTO.getOrderNumber());
        if (byOrderNum.isPresent())  throw new ItemAlreadyExists("This Region order number is exist");
    RegionEntity regionEntity=toEntity(regionDTO);
    regionRepository.save(regionEntity);
    regionDTO.setId(regionEntity.getId());
    regionDTO.setVisible(regionEntity.getVisible());
    regionDTO.setCreatedDate(regionEntity.getCreatedDate());
    return regionDTO;
    }
    //2 update region by Admin

    public String updateRegion(RegionDTO regionDTO, Integer id){
       Optional <RegionEntity> optional=regionRepository.findById(id);
       if(optional.isEmpty()){
           throw new ItemNotFoundException("Region not found!");
       }
        Optional<RegionEntity> checkOption=regionRepository
                .findByNameEngOrNameUzOrNameRu(regionDTO.getNameEng(),regionDTO.getNameRu(),regionDTO.getNameUz());
        if (checkOption.isPresent()) throw new ItemAlreadyExists("This Region already exists");
       RegionEntity regionEntity=optional.get();
        if(regionDTO.getNameEng()!=null){
           regionEntity.setNameEng(regionDTO.getNameEng());
        }
        if (regionDTO.getNameUz()!=null){
            regionEntity.setNameUz(regionDTO.getNameUz());
        }
        if(regionDTO.getNameRu()!=null){
            regionEntity.setNameRu(regionDTO.getNameRu());
        }
        if(regionDTO.getVisible()!=null){
            regionEntity.setVisible(regionDTO.getVisible());
        }
        if (regionDTO.getOrderNumber()!=null){
            regionEntity.setOrderNumber(regionDTO.getOrderNumber());
        }
        regionRepository.save(regionEntity);
        return "Region updated";
    }
    //3 delete by Admin
    public String deleteRegion(Integer id){
        Optional <RegionEntity> optional=regionRepository.findById(id);
        if(optional.isEmpty()){
            throw new ItemNotFoundException("Region not found!");
        }
        if (!optional.get().getVisible()) throw new ItemAlreadyExists("Region already deleted");
        int result=regionRepository.deleteRegionById(id);
        return result>0?"region deleted":"region not deleted";
    }
    //4 region list
    public List<RegionDTO>getAllRegion(){
        Iterable<RegionEntity> iterable=regionRepository.findAll();
        List<RegionDTO> dtoList=new LinkedList<>();
        iterable.forEach(s->dtoList.add(toDto(s)));
        return dtoList;
    }
    //5 getBy language
    public List<RegionLanguageMapper>getByLanguage(String  language){
        if(language==null) throw new AppBadRequestException("Enter language!");
        if(language.toLowerCase().startsWith("eng")){
          return regionRepository.getByEnglish();
        }
        else if(language.toLowerCase().startsWith("uz")){
           return regionRepository.getByUz();
        }
        else if(language.toLowerCase().startsWith("ru")){
            return regionRepository.getByRussian();
        }
        return null;
    }
    private RegionEntity toEntity(RegionDTO dto){
        RegionEntity regionEntity=new RegionEntity();
        regionEntity.setNameEng(dto.getNameEng());
        regionEntity.setNameUz(dto.getNameUz());
        regionEntity.setNameRu(dto.getNameRu());
        regionEntity.setOrderNumber(dto.getOrderNumber());
        return regionEntity;
    }
    private RegionDTO toDto(RegionEntity regionEntity){
        RegionDTO regionDTO=new RegionDTO();
        regionDTO.setNameUz(regionEntity.getNameUz());
        regionDTO.setNameRu(regionEntity.getNameRu());
        regionDTO.setNameEng(regionEntity.getNameEng());
        regionDTO.setOrderNumber(regionEntity.getOrderNumber());
        regionDTO.setVisible(regionEntity.getVisible());
        regionDTO.setCreatedDate(regionEntity.getCreatedDate());
        regionDTO.setId(regionEntity.getId());
        return regionDTO;
    }



}
