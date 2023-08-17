package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseIdentityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "visible")
    private Boolean visible=true;
    @Column(name = "prt_id")
    private Integer prtId;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
}
