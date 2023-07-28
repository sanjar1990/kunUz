package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.folder.name}")
    private String folderName;
    @Value("${attach.url}")
    private String attachUrl;
    // 1 upload any
    public AttachDTO upload(MultipartFile file){
        if (file.isEmpty()) throw new ItemNotFoundException("file not found");
    String pathFolder=getYMD();
        File folder=new File(folderName+"/"+pathFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String key= UUID.randomUUID().toString();
        String extension=getExtension(file.getOriginalFilename());
        System.out.println(extension);
        System.out.println(file.getContentType());
        try {
            byte [] bytes=file.getBytes();
            Path path= Paths.get(folderName+"/"+pathFolder+"/"+key+"."+extension);
            Files.write(path,bytes);
            AttachEntity attachEntity=new AttachEntity();
            attachEntity.setId(key);
            attachEntity.setSize(file.getSize());
            attachEntity.setExtension(extension);
            attachEntity.setOriginalName(file.getOriginalFilename());
            attachEntity.setPath(folderName+"/"+pathFolder+"/"+key+"."+extension);
            attachRepository.save(attachEntity);
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(key);
            attachDTO.setCreatedDate(attachEntity.getCreatedDate());
            attachDTO.setPath(attachUrl);
            attachDTO.setSize(file.getSize());
            attachDTO.setExtension(extension);
            attachDTO.setOriginalName(file.getOriginalFilename());
            attachDTO.setUrl(getUrl(key));
            return attachDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //2 open by id
    public byte[] openById(String id){
    AttachEntity attachEntity=get(id);
    try {
    String url=attachEntity.getPath();
        BufferedImage image= ImageIO.read(new File(url));
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(image,attachEntity.getExtension(), baos);
        baos.flush();
        byte [] bytes=baos.toByteArray();
        baos.close();
        return bytes;
    } catch (IOException e) {
        e.printStackTrace();
        return new byte[0];
    }
    }
    //3. Open general (by id)

    public byte[]openByIdGeneral(String id){
        AttachEntity attachEntity=get(id);
        try {
            String url =attachEntity.getPath();
            File file=new File(url);
            byte[]bytes=new byte[(int) file.length()];
            FileInputStream fileInputStream=new FileInputStream(url);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    //5. Pagination (ADMIN)
    public PageImpl<AttachDTO>pagination(Integer page, Integer size){
        Sort sort=Sort.by(Sort.Direction.ASC,"createdDate");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<AttachEntity>pageObj=attachRepository.findAll(pageable);
        List<AttachDTO>dtoList=pageObj.getContent().stream().map(s->{
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(s.getId());
            attachDTO.setCreatedDate(s.getCreatedDate());
            attachDTO.setPath(s.getPath());
            attachDTO.setSize(s.getSize());
            attachDTO.setExtension(s.getExtension());
            attachDTO.setOriginalName(s.getOriginalName());
            attachDTO.setUrl(getUrl(s.getId()));
            return attachDTO;
        }).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }

    //6. Delete by id (delete from system and table) (ADMIN)
    public Boolean deleteById(String id){
        boolean t=false;
        AttachEntity attachEntity=get(id);
        attachRepository.deleteById(id);
        File file=new File(attachEntity.getPath());
        if (file.exists()){
          t= file.delete();
        }
        return t;
    }
     //4. Download (by id  with origin name)
    public ResponseEntity<Resource> download(String id) {
        AttachEntity attachEntity=get(id);
        try {
            Path file=Paths.get(attachEntity.getPath());
            Resource resource=new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){

                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+attachEntity.getOriginalName()+"\"").body(resource);
            }else {
                throw new AppBadRequestException("could not read the file");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getUrl(String id){
        return attachUrl+"/open/"+id+"/img";
    }
    public AttachDTO getAttachURL(String id){
         if(id==null){
             return null;
         }
         AttachDTO attachDTO=new AttachDTO();
         attachDTO.setId(id);
         attachDTO.setUrl(getUrl(id));
         return attachDTO;
    }
    private AttachEntity get(String id){
        return attachRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Attach not found!"));
    }
    private String getYMD(){
        int year= Calendar.getInstance().get(Calendar.YEAR);
        int month=Calendar.getInstance().get(Calendar.MONTH);
        int day=Calendar.getInstance().get(Calendar.DATE);
        return year+"/"+month+"/"+day;
    }

    private String getExtension(String fileName){
    int lastIndex=fileName.lastIndexOf(".");
    return fileName.substring(lastIndex+1);
    }


}
