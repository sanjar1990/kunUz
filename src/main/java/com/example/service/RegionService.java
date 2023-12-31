package com.example.service;
import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.RegionLanguageMapper;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    //1 create By Admin
    public RegionDTO createRegion(RegionDTO regionDTO, Integer prtId){

        Boolean exists=regionRepository
                .existsAllByNameEnOrNameUzOrNameRu(regionDTO.getNameEn(),regionDTO.getNameRu(),regionDTO.getNameUz());
        if (exists) throw new ItemAlreadyExists("This Region already exists");
        Optional<RegionEntity>byOrderNum=regionRepository.findByOrderNumber(regionDTO.getOrderNumber());
        if (byOrderNum.isPresent())  throw new ItemAlreadyExists("This Region order number is exist");
    RegionEntity regionEntity=toEntity(regionDTO);
    regionEntity.setPrtId(prtId);
    regionRepository.save(regionEntity);
    regionDTO.setId(regionEntity.getId());
    regionDTO.setVisible(regionEntity.getVisible());
    regionDTO.setCreatedDate(regionEntity.getCreatedDate());
    regionDTO.setPrtId(prtId);
    return regionDTO;
    }
    //2 update region by Admin
    @Cacheable(value = "region",key = "#id")
    public RegionDTO updateRegion(RegionDTO regionDTO, Integer id,Integer prtId){
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
        regionEntity.setPrtId(prtId);
        regionRepository.save(regionEntity);
        regionDTO.setId(id);
        return regionDTO;
    }
    //3 delete by Admin
    @CacheEvict(value = "region",key = "#id")
    public String deleteRegion(Integer id){
        return regionRepository.deleteRegionById(id)>0?"region deleted":"region not deleted";
    }
    //4 region list
    @Cacheable(cacheNames ="region")
    public List<RegionDTO>getAll(){
       return regionRepository.findAllByVisibleTrueOrderByOrderNumberAsc()
               .stream().map(s->toDto(s)).toList();
    }
    //5 getBy language
    @Cacheable(value = "region",key = "#language")
    public List<RegionDTO>getByLanguage(Language language){
        if(language==null) throw new AppBadRequestException("Enter language!");
       List<RegionLanguageMapper> regionLanguageMapper= regionRepository.getByLanguage(language.name().toLowerCase());
       List<RegionDTO> dtoList=new LinkedList<>();
       for (RegionLanguageMapper r: regionLanguageMapper){
           RegionDTO regionDTO=new RegionDTO();
           regionDTO.setId(r.getId());
           regionDTO.setOrderNumber(r.getOrderNumber());
           regionDTO.setName(r.getName());
           dtoList.add(regionDTO);
       }
       return dtoList;
    }

    //get by id
    @Cacheable(value = "region",key = "#id")
    public RegionDTO getById(Integer id){
        return toDto(getRegionEntity(id));
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
        regionDTO.setPrtId(regionEntity.getPrtId());
        return regionDTO;
    }
    private RegionEntity getRegionEntity(Integer id){
        return regionRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(()-> new ItemNotFoundException("region not found"));
    }
//    @CacheEvict(value = "region",allEntries = true)
    public void deleteAll(Integer id){
       regionRepository.deleteAll();
    }

}
