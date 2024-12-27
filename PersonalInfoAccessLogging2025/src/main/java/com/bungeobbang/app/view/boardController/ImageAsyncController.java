package com.bungeobbang.app.view.boardController;

import com.bungeobbang.app.biz.imageFile.ImageFileDTO;
import com.bungeobbang.app.view.util.FileUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class ImageAsyncController {

    private String FOLDER_PATH = "uploads/board/"; //webapp기준

    //이미지 저장
    @RequestMapping("/addImage.do")
    public @ResponseBody String addImage(HttpServletRequest request, ImageFileDTO imageFileDTO) {
        log.info("log: /addImage.do addImage - start");
        log.info("log: /addImage.do addImage - param imageFileDTO: " + imageFileDTO);
        //폴더명은 게시글 작성 페이지가 열릴 때 view에 전달
        MultipartFile file = imageFileDTO.getFile();
        String fileName;
        String src; //반환할 경로

        try {//프로그램 비정상 종료를 막을 try catch
            fileName = FileUtil.insertFile(request.getServletContext(), FOLDER_PATH+imageFileDTO.getBoardFolder(), file, FileUtil.createFileName());
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("log: addImage - error insertFile() NPE or Illeagl : "+e.getMessage());
            return null;
        }
        src = request.getContextPath() + "/" + FOLDER_PATH + imageFileDTO.getBoardFolder() + "/" + fileName;
        log.info("log: addImage - src: [{}]", src);
        log.info("log: /addImage.do addImage - end");
        //서버 주소 경로 반환 img src내용
        return src;
    }
}
