package com.example.service;

import com.example.dto.CommentLikeDTO;
import com.example.entity.CommentLikeEntity;
import com.example.enums.CommentLikeStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentLikeDTO like(String commentId, Integer profileId) {
        CommentLikeEntity entity;
       Optional<CommentLikeEntity>optional=get(commentId,profileId);
        CommentLikeDTO dto=new CommentLikeDTO();
       CommentLikeStatus status=null;
        if (optional.isEmpty()){
           entity =new CommentLikeEntity();
           status=CommentLikeStatus.LIKE;
        }else {
           entity=optional.get();
            if(entity.getStatus().equals(CommentLikeStatus.DISLIKE)){
                status=CommentLikeStatus.LIKE;
            }else if(entity.getStatus().equals(CommentLikeStatus.LIKE)){
                commentLikeRepository.deleteById(entity.getId());
                return dto;
            }
        }
        entity.setStatus(status);
        entity.setProfileId(profileId);
        entity.setCommentId(commentId);
        commentLikeRepository.save(entity);
        dto.setId(entity.getId());
        dto.setStatus(CommentLikeStatus.LIKE);
        dto.setCommentId(commentId);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(profileId);
        entity.setProfileId(profileId);
        return dto;
    }

    public CommentLikeDTO dislike(String commentId, Integer profileId) {
        CommentLikeEntity entity;
        Optional<CommentLikeEntity>optional=get(commentId,profileId);
        CommentLikeDTO dto=new CommentLikeDTO();
        CommentLikeStatus status=null;
        if (optional.isEmpty()){
            entity =new CommentLikeEntity();
            status=CommentLikeStatus.DISLIKE;
        }else {
            entity=optional.get();
            if(entity.getStatus().equals(CommentLikeStatus.DISLIKE)){
                commentLikeRepository.deleteById(entity.getId());
                return dto;
            }else if(entity.getStatus().equals(CommentLikeStatus.LIKE)){
                status=CommentLikeStatus.DISLIKE;
            }
        }
        entity.setStatus(status);
        entity.setProfileId(profileId);
        entity.setCommentId(commentId);
        commentLikeRepository.save(entity);
        dto.setId(entity.getId());
        dto.setStatus(CommentLikeStatus.LIKE);
        dto.setCommentId(commentId);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(profileId);
        entity.setProfileId(profileId);
        return dto;
    }
    public String remove(String id, Integer profileId) {
        return commentLikeRepository.deleteByIdAndProfileId(id,profileId)>0?"deleted":"not deleted";
    }
    //check
    private Optional<CommentLikeEntity> get(String commentId, Integer profileId){
        return commentLikeRepository.findAllByCommentIdAndProfileIdAndVisibleTrue(commentId,profileId);
    }
}
