package com.example.repository;

import com.example.dto.FilterProfileDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;
    //ByAdmin
    public FilterResultDTO<ProfileEntity> filterPagination(FilterProfileDTO filterProfileDTO, Integer page, Integer size){
        StringBuilder selectBuilder=new StringBuilder("from ProfileEntity as p");
        StringBuilder countBuilder=new StringBuilder("select count(p) from ProfileEntity as p");
        StringBuilder builder=new StringBuilder(" where visible=true");
        Map<String,Object> params=new HashMap<>();
        if(filterProfileDTO.getName()!=null){
            builder.append(" and p.name=:name");
            params.put("name",filterProfileDTO.getName());
        }
        if(filterProfileDTO.getSurname()!=null){
            builder.append(" and p.surname=:name");
            params.put("name", filterProfileDTO.getSurname());
        }
        if(filterProfileDTO.getPhone()!=null){
            builder.append(" and p.phone=:phone");
            params.put("phone",filterProfileDTO.getPhone());
        }
        if(filterProfileDTO.getRole()!=null){
            builder.append(" and p.role=:role");
            params.put("role",filterProfileDTO.getRole());
        }
        if (filterProfileDTO.getCreated_date_from()!=null){
            builder.append(" and p.createdDate>=:from");
            params.put("from",filterProfileDTO.getCreated_date_from().atStartOfDay());
        }
        if(filterProfileDTO.getCreated_date_to()!=null){
            builder.append(" and p.createdDate<=:to");
            params.put("to", LocalDateTime.of(filterProfileDTO.getCreated_date_to(), LocalTime.MAX));
        }
        countBuilder.append(builder);
        builder.append(" order by p.createdDate");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String,Object> p: params.entrySet()){
            selectQuery.setParameter(p.getKey(),p.getValue());
            countQuery.setParameter(p.getKey(),p.getValue());
        }
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        List<ProfileEntity> entityList=selectQuery.getResultList();
        Long totalElement=(Long) countQuery.getSingleResult();
        return new FilterResultDTO<>(entityList, totalElement);
    }

}
