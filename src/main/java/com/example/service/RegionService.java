package com.example.service;
import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
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
        Boolean exists=regionRepository
                .existsAllByNameEnOrNameUzOrNameRu(regionDTO.getNameEn(),regionDTO.getNameRu(),regionDTO.getNameUz());
        if (exists) throw new ItemAlreadyExists("This Region already exists");
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
       RegionEntity regionEntity=getRegionEntity(id);
    Boolean exists=regionRepository
            .existsAllByNameEnOrNameUzOrNameRu(regionDTO.getNameEn(),regionDTO.getNameRu(),regionDTO.getNameUz());
        if (exists) throw new ItemAlreadyExists("This Region already exists");
        if(regionDTO.getNameEn()!=null){
           regionEntity.setNameEn(regionDTO.getNameEn());
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
        return regionRepository.deleteRegionById(id)>0?"region deleted":"region not deleted";
    }
    //4 region list
    public List<RegionDTO>getAllRegion(){
       return regionRepository.findAllByVisibleTrueOrderByOrderNumberAsc().stream().map(s->toDto(s)).toList();
    }
    //5 getBy language
    public List<RegionDTO>getByLanguage(Language language){
        if(language==null) throw new AppBadRequestException("Enter language!");
       List<RegionLanguageMapper> regionLanguageMapper= regionRepository.getByLanguage(language.name().toLowerCase());
       List<RegionDTO> dtoList=new LinkedList<>();
       for (RegionLanguageMapper r: regionLanguageMapper){
           RegionDTO regionDTO=new RegionDTO();
           regionDTO.setId(r.getId());
           regionDTO.setOrderNumber(r.getOrderNumber());
           regionDTO.setRegionName(r.getName());
           dtoList.add(regionDTO);
       }
       return dtoList;
    }
    private RegionEntity toEntity(RegionDTO dto){
        RegionEntity regionEntity=new RegionEntity();
        regionEntity.setNameEn(dto.getNameEn());
        regionEntity.setNameUz(dto.getNameUz());
        regionEntity.setNameRu(dto.getNameRu());
        regionEntity.setOrderNumber(dto.getOrderNumber());
        return regionEntity;
    }
    private RegionDTO toDto(RegionEntity regionEntity){
        RegionDTO regionDTO=new RegionDTO();
        regionDTO.setNameUz(regionEntity.getNameUz());
        regionDTO.setNameRu(regionEntity.getNameRu());
        regionDTO.setNameEn(regionEntity.getNameEn());
        regionDTO.setOrderNumber(regionEntity.getOrderNumber());
        regionDTO.setVisible(regionEntity.getVisible());
        regionDTO.setCreatedDate(regionEntity.getCreatedDate());
        regionDTO.setId(regionEntity.getId());
        return regionDTO;
    }
    private RegionEntity getRegionEntity(Integer regionId){
        return regionRepository.findByIdAndVisibleTrue(regionId)
                .orElseThrow(()-> new ItemNotFoundException("region not found"));
    }


}
