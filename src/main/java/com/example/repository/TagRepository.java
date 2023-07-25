package com.example.repository;


import com.example.entity.TagEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {
    Optional<TagEntity> findByIdAndVisibleTrue(Integer id);
    List<TagEntity> findAllByVisibleTrue();
    @Transactional
    @Modifying
    @Query("update TagEntity set visible=false where id=?1")
    int deleteTag(Integer id);
    Optional<TagEntity>findByNameAndVisibleTrue(String name);
}
