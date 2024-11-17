package com.bungeobbang.app.biz.imageFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString
public class ImageFileDTO {
    private MultipartFile file;
    private String boardFolder;
}
