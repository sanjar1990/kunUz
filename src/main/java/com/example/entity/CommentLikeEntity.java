package com.example.entity;

import com.example.enums.CommentLikeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment_like")
public class CommentLikeEntity extends BaseStringEntity {
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "comment_id")
    private String commentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentLikeStatus status;
}
