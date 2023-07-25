package com.example.repository;

<<<<<<< HEAD

import com.example.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;

public interface AttachRepository extends CrudRepository<AttachEntity,String > {
=======
import com.example.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttachRepository extends CrudRepository<AttachEntity, String >,
        PagingAndSortingRepository<AttachEntity, String> {

>>>>>>> attachPractise

}
