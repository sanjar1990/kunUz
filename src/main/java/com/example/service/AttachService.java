package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
<<<<<<< HEAD
import com.example.exception.AppBadRequestException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.example.exception.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
>>>>>>> attachPractise
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
=======
import java.io.*;
>>>>>>> attachPractise
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> attachPractise
import java.util.UUID;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    private final String folderName="attaches";
<<<<<<< HEAD
    public String saveToSystem(MultipartFile file) {
//        System.out.println(file.getSize());
//        System.out.println(file.getName());
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getContentType());
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] loadImage(String fileName) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("attaches/" + fileName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }
    public AttachDTO save(MultipartFile file) {
        String pathFolder = getYmDString(); // 2022/04/23
        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
        String extension = getExtension(file.getOriginalFilename()); // jpg
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            // attaches/2022/04/23/dasdasd-dasdasda-asdasda-asdasd.jpg
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setId(key);
            entity.setPath(pathFolder); // 2022/04/23
            entity.setSize(file.getSize());
            entity.setOriginalName(file.getOriginalFilename());
            entity.setExtension(extension);
            attachRepository.save(entity);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(key);
            attachDTO.setOriginalName(entity.getOriginalName());
            // any think you want mazgi.
            attachDTO.setUrl("");

            return attachDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public byte[] loadImageById(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            BufferedImage originalImage = ImageIO.read(new File(url));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, entity.getExtension(), baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }
    public byte[] loadByIdGeneral(String id) {
        AttachEntity entity = get(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            File file = new File(url);

            byte[] bytes = new byte[(int) file.length()];

            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }
    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("File not found");
        });
=======
    // 1 upload any
    public AttachDTO upload(MultipartFile file){
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
            attachEntity.setPath(path.toString());
            attachRepository.save(attachEntity);
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(key);
            attachDTO.setCreatedDate(attachEntity.getCreatedDate());
            attachDTO.setPath(path.toString());
            attachDTO.setSize(file.getSize());
            attachDTO.setExtension(extension);
            attachDTO.setOriginalName(file.getOriginalFilename());
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
>>>>>>> attachPractise
    }
}
