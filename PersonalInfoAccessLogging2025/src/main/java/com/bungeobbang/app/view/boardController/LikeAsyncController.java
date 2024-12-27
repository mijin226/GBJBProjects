package com.bungeobbang.app.view.boardController;

import com.bungeobbang.app.biz.like.LikeDTO;
import com.bungeobbang.app.biz.like.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LikeAsyncController {
    @Autowired
    private LikeService likeService;

    //좋아요 추가
    @RequestMapping("/addLike.do")
    public @ResponseBody boolean addLike(@RequestBody LikeDTO likeDTO) {
        log.info("log: /addLike.do addLike likeDTO: {}", likeDTO);
        return likeService.insert(likeDTO);
    }

    //좋아요 삭제 (조회해서 받아온 num 전달)
    @RequestMapping("/deleteLike.do")
    public @ResponseBody boolean deleteLike(LikeDTO likeDTO) {
        log.info("log: /deleteLike.do deleteLike likeDTO: {}", likeDTO);
        return likeService.delete(likeDTO);
    }

    //좋아요 조회
    @RequestMapping("/infoLike.do")
    public @ResponseBody int infoLike(@RequestBody LikeDTO likeDTO) {
        log.info("log: /infoLike.do infoLike likeDTO: {}", likeDTO);
        likeDTO = likeService.selectOne(likeDTO);
        if(likeDTO == null) {
            return -1;
        }
        return likeDTO.getLikeNum();
    }

    //좋아요 개수 조회
    @RequestMapping("/infoLikeCnt.do")
    public @ResponseBody int infoLikeCnt( LikeDTO likeDTO) {
        likeDTO.setCondition("CNT");
        log.info("log: /infoLikeCnt.do infoLike likeDTO: {}", likeDTO);
        likeDTO = likeService.selectOne(likeDTO);
        if(likeDTO == null) {
            return -1;
        }
        return likeDTO.getCnt();
    }
}
