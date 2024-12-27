package com.bungeobbang.app.view.util;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

/**@author 한지윤 */
@Slf4j
public class FileUtil {

    //UUID와 현재 시간으로 String 반환
    public static String createFileName(){
        log.info("log: createFileName start");
        String fileName = LocalDate.now() + "+" +  UUID.randomUUID(); //파일명 구성
        log.info("log: file name [{}]", fileName);
        log.info("log: createFileName end");
        return fileName;
    }

    //해당 폴더 하위의 폴더와 파일 삭제
    public static boolean deleteFileAndDirectory (File folder) {
        log.info("log: deleteFile - start");
        //서버에 해당 경로의 폴더가 있다면
        if(folder.exists()) {
            File[] files = folder.listFiles(); //해당 폴더의 파일리스트 데이터
            if(files != null) { //빈 폴더가 아니라면
                for(File file : files) {
                    log.info("log: deleteFile - board image file delete file : [{}]", file);
                    if(file.isDirectory()){ //만약 파일이 아니라 폴더라면
                        log.info("log: deleteFile - {} is directory", file);
                        //재귀함수
                        deleteFileAndDirectory(file);
                    }
                    if(!file.delete()){ //해당 파일 삭제
                        //파일 삭제 실패 시
                        //개발자에게 안내
                        log.error("log: deleteFile - board image file delete fail!!!! file : [{}]", file.getPath());
                    }
                }
                if(!folder.delete()){
                    //폴더 삭제 실패 시 개발자에게 안내
                    log.error("log: deleteFile - board image folder delete fail!!!! folder : [{}]", folder.getPath());
                    return false;
                }
            }
        }
        else{
            //해당 경로에 폴더가 존재하지 않음을 안내
            log.error("log: deleteFile - no image folder error imagePath: [{}]", folder.getPath());
            return false;
        }
        log.info("log: deleteFile - end / true");
        return true;
    }

    //입력받은 경로에 입력받은 파일을 저장 후 성공하면 파일명, 실패하면 null값 반환, 비체크 예외이므로 사용 시 try catch 잊지말기
    public static String insertFile (ServletContext servletContext, String path, MultipartFile file, String fileName) throws NullPointerException, IllegalArgumentException {
        log.info("log: insertFile - start");

        //각기 다른 작업환경에서도 원활하게 구동될 수 있도록 경로를 받아와 설정
        File folder = new File(servletContext.getRealPath(path)); //완성된 경로
        if(!folder.exists()) { //해당 경로가 존재하지 않는다면
            if(!folder.mkdirs()){ //해당 경로에 필요한 폴더 생성
                //생성 실패
                log.error("log: insertFile - mkdirs Fail folder : [{}]", folder.getPath());
                return null;
            }
        }
        String extension = ""; //확장자, 만약 문자열에 .이 없다면 초기값
        String originalFilename = file.getOriginalFilename();
        if(originalFilename != null && originalFilename.contains(".")){ //.이 있는 문자열이라면
            log.info("log: createFileName file contains '.'");
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));//확장자 추출
        }

        String newFileName = fileName + extension; //DB에 저장하는 경우를 위한 String 값
        try {
            file.transferTo(new File(folder, newFileName)); //지정된 경로에 전달받은 이름, 추출한 확장자로 저장
        } catch (IOException e) {
            log.error("log: insertFile - file save fail");
            return null;
        }
        log.info("log: insertFile - end / return String : [{}]", newFileName);
        return newFileName;
    }
}
