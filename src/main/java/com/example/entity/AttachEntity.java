package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "attach")
public class AttachEntity {
    @Id
    private String id;
    @Column
    private String originalName;
    @Column
    private String path;
    @Column
    private Long size;
    @Column
    private String extension;
    @Column
    private LocalDateTime createdDate=LocalDateTime.now();
}
