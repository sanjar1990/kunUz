package com.example.service;

import com.example.dto.TagDTO;
import com.example.entity.TagEntity;
import com.example.exception.ItemNotFoundException;
import com.example.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(TagDTO tagDTO){
        TagEntity tagEntity = new TagEntity();
        if(tagDTO.getName()!=null){
            tagEntity.setName(tagDTO.getName());
        }
        tagRepository.save(tagEntity);
        tagDTO.setId(tagEntity.getId());
        return tagDTO;
    }

    public TagDTO getById(Integer id){
        Optional<TagEntity> optional=tagRepository.findByIdAndVisibleTrue(id);
        if(optional.isEmpty()) throw new ItemNotFoundException("Tag is not found");
        return toDto(optional.get());
    }
    public List<TagDTO> getAll(){
        List<TagEntity> list=tagRepository.findAllByVisibleTrue();
        return list.stream().map(s->toDto(s)).toList();
    }
    public String delete(Integer id){
        return tagRepository.deleteTag(id)>0?"Tag is deleted":"tag is not deleted";
    }
    private TagDTO toDto(TagEntity tagEntity){
        TagDTO tagDTO=new TagDTO();
        tagDTO.setId(tagEntity.getId());
        tagDTO.setName(tagEntity.getName());
        return tagDTO;
    }
}
