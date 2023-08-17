package com.example.entity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseStringEntity {

    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "shared_count")
    private Integer sharedCount;
    @Column(name = "image_id")
    private String imageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", insertable = false,updatable = false)
    private AttachEntity image;
    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;
    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;
    @Column(name = "moderator_id", nullable = false)
    private Integer moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;
    @Column(name = "publisher_id")
    private Integer publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status=ArticleStatus.NOTPUBLISHED;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "view_count")
    private Integer viewCount;
    @OneToMany(mappedBy = "article")
    private List<ArticleTypesEntity> articleTypeList;
    @OneToMany(mappedBy = "article")
    private List<ArticleTagEntity> articleTagList;
    @Column(name = "like_count")
    private int likeCount;
    @Column(name = "dislike_count")
    private int dislikeCount;

}
