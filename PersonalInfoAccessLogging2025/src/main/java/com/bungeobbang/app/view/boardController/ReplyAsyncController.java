package com.bungeobbang.app.view.boardController;

import com.bungeobbang.app.biz.reply.ReplyDTO;
import com.bungeobbang.app.biz.reply.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Slf4j
public class ReplyAsyncController {
    @Autowired
    private ReplyService replyService;

    //댓글 추가
    @RequestMapping("/addReply.do")
    public @ResponseBody String addReply(@RequestBody ReplyDTO replyDTO) {
        log.info("log: /addReply.do addReply - replyDTO: {}", replyDTO);
        return replyService.insert(replyDTO)+"";
    }

    //댓글 삭제
    @RequestMapping("/deleteReply.do")
    public @ResponseBody String deleteReply(@RequestBody ReplyDTO replyDTO) {
        log.info("log: /deleteReply.do deleteReply - replyDTO: {}", replyDTO);
        //작성자 확인 AOP 대상
        return replyService.delete(replyDTO)+"";
    }

    //댓글 리스트
    @RequestMapping("/loadListReply.do")
    public @ResponseBody ArrayList<ReplyDTO> listReply(@RequestBody ReplyDTO replyDTO) {
        log.info("log: /loadListReply.do listReply - replyDTO: {}", replyDTO);
        return replyService.selectAll(replyDTO);
    }
}
