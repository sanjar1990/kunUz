package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BaseStringEntity {
  @Column(name = "message", columnDefinition = "TEXT")
  private String message;
  @Column(name = "email")
  private String email;
}
