package com.example.service;

import com.example.dto.*;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.CommentRepository;
import com.example.repository.CustomCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CustomCommentRepository customCommentRepository;

    public CommentDTO create(Integer profileId, CommentDTO dto) {
        CommentEntity entity=new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(profileId);
        entity.setReplyId(dto.getReplyId());
        commentRepository.save(entity);
        dto.setId(entity.getId());
        dto.setProfileId(profileId);
        return dto;
    }
//            2. UPDATE (ANY and owner)
//         (content,article_id)
    public CommentDTO update(String commentId, Integer profileId, CommentDTO dto) {
        CommentEntity entity=get(commentId);
        if(!entity.getProfileId().equals(profileId)){
            throw new AppBadRequestException("Comment owner not match");
        }
        entity.setArticleId(dto.getArticleId());
        entity.setContent(dto.getContent());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);
        dto.setId(entity.getId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setReplyId(entity.getReplyId());
        dto.setProfileId(profileId);
        return dto;
    }
    public String  delete(String commentId, Integer profileId) {
        ProfileEntity profileEntity=profileService.getProfileEntity(profileId);
        CommentEntity entity=get(commentId);
        if(profileEntity.getRole().equals(ProfileRole.ROLE_ADMIN) || entity.getProfileId().equals(profileId)){
            int n=commentRepository.deleteComment(commentId);
            return n==1?"comment deleted":"comment not deleted";
        }
        throw new AppBadRequestException("You can't delete comment");
    }
    private CommentEntity get(String commentId){
        return commentRepository.findById(commentId).orElseThrow(()->new ItemNotFoundException("comment not found"));
    }

//     4. Get Article Comment List By Article Id
//    id,created_date,update_date,profile(id,name,surname)
    public List<CommentDTO> getByArticleId(String articleId) {

        List<CommentEntity> entityList=commentRepository.getWithArticleId(articleId);
        System.out.println(entityList);
        return entityList.stream().map(s->{
            CommentDTO commentDTO=new CommentDTO();
            commentDTO.setId(s.getId());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setUpdateDate(s.getUpdateDate());
            ProfileDTO profileDTO=new ProfileDTO();
            profileDTO.setId(s.getProfile().getId());
            profileDTO.setName(s.getProfile().getName());
            profileDTO.setSurname(s.getProfile().getSurname());
            commentDTO.setProfileDTO(profileDTO);
            return commentDTO;
        }).toList();
    }
//  5. Comment List (pagination) (ADMIN)
//            (id,created_date,update_date,profile(id,name,surname),content,article(id,title),reply_id,)
    public PageImpl<CommentDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<CommentEntity> pageObj=commentRepository.findAllByVisibleTrue(pageable);
        List<CommentDTO> dtoList=pageObj.getContent().stream().map(s->{
            CommentDTO commentDTO=new CommentDTO();
            commentDTO.setId(s.getId());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setUpdateDate(s.getUpdateDate());
            ProfileDTO profileDTO=new ProfileDTO();
            profileDTO.setId(s.getProfileId());
            profileDTO.setName(s.getProfile().getName());
            profileDTO.setSurname(s.getProfile().getSurname());
            commentDTO.setProfileDTO(profileDTO);
            commentDTO.setContent(s.getContent());
            ArticleDTO articleDTO=new ArticleDTO();
            articleDTO.setId(s.getArticleId());
            articleDTO.setTitle(s.getArticle().getTitle());
            commentDTO.setArticleDTO(articleDTO);
            commentDTO.setReplyId(s.getReplyId());
            return commentDTO;
        }).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
//    6. Comment Filter(id,created_date_from,created_date_to,profile_id,article_id) with Pagination (ADMIN)
//    id,created_date,update_date,profile_id,content,article_id,reply_id,visible
    public PageImpl<CommentDTO> filterPagination(FilterCommentDTO filterCommentDTO, Integer page, Integer size) {
        FilterResultDTO<CommentEntity> result=customCommentRepository.filterPagination(filterCommentDTO,page,size);
        Pageable pageable=PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,"createdDate"));
        List<CommentDTO> dtoList=result.getContent().stream().map(s->{
            CommentDTO commentDTO=new CommentDTO();
            commentDTO.setId(s.getId());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setUpdateDate(s.getUpdateDate());
            commentDTO.setProfileId(s.getProfileId());
            commentDTO.setContent(s.getContent());
            s.setArticleId(s.getArticleId());
            s.setReplyId(s.getReplyId());
            s.setVisible(s.getVisible());
            return commentDTO;
        }).toList();
        return new PageImpl<>(dtoList,pageable,result.getTotalElement());

    }
//     7. Get Replied Comment List by Comment Id
//    id,created_date,update_date,profile(id,name,surname)

    public List<CommentDTO> getRepliedComment(String commentId) {
        List<CommentEntity> entityList=commentRepository.findAllByReplyIdAndVisibleTrue(commentId);
        return entityList.stream().map(s->{
            CommentDTO commentDTO= new CommentDTO();
          commentDTO.setId(s.getId());
          commentDTO.setCreatedDate(s.getCreatedDate());
          commentDTO.setUpdateDate(s.getUpdateDate());
          ProfileDTO profileDTO=new ProfileDTO();
          profileDTO.setId(s.getProfile().getId());
          profileDTO.setName(s.getProfile().getName());
          profileDTO.setSurname(s.getProfile().getSurname());
          commentDTO.setProfileDTO(profileDTO);
          return commentDTO;
        }).toList();
    }
}
