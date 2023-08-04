package com.example.entity;

import com.example.enums.SmsStatus;
import com.example.enums.SmsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity extends BaseStringEntity {
    @Column(name = "phone")
    private String phone;
    @Column(name = "message")
    private String message;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SmsStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SmsType type;
}
