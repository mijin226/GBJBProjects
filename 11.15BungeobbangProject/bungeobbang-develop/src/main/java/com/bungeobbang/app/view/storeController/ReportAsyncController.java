package com.bungeobbang.app.view.storeController;

import com.bungeobbang.app.biz.declare.DeclareDTO;
import com.bungeobbang.app.biz.declare.DeclareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportAsyncController {
    @Autowired
    private DeclareService declareService;

    //폐점 신고
    @RequestMapping("/addReport.do")
    public @ResponseBody String addReport(DeclareDTO declareDTO){
        log.info("log: /addReport.do addReport.do - start");
        declareDTO.setDeclareContent("this store is closed"); //현재 신고가 하나 뿐이라 하드코딩
        log.info("log: addReport.do - declareDTO : {}", declareDTO);
        log.info("log: /addReport.do addReport.do - end");
        return declareService.insert(declareDTO)+""; //결과값 text로 반환
    }
}
