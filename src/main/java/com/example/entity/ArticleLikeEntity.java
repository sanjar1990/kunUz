package com.example.entity;

import com.example.enums.ArticleLiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "article_like")
public class ArticleLikeEntity extends BaseStringEntity{
    @Column(name = "profile_id")
    private Integer profileId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",updatable = false,insertable = false)
    private ProfileEntity profile;
    @Column(name = "article_id")
    private String articleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",updatable = false,insertable = false)
    private ArticleEntity article;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleLiceStatus status;
}
