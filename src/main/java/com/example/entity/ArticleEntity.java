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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private ProfileEntity publisherId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status=ArticleStatus.NOTPUBLISHED;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private boolean visible=true;
    @Column(name = "view_count")
    private Integer viewCount;
    @ManyToMany
    @JoinTable(
            name = "Article_releted_Types",
            joinColumns =@JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "article_type_id")
    )
    private List<ArticleTypeEntity> articleTypes;

}
