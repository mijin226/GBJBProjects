package com.bungeobbang.app.view.memberController;

import com.bungeobbang.app.biz.member.MemberDTO;
import com.bungeobbang.app.biz.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MemberAsyncController {
    @Autowired
    private MemberService memberService;

    //이메일 중복확인
    @RequestMapping(value = "/checkEmailName.do", method = RequestMethod.POST)
    public @ResponseBody String emailNameCheck(MemberDTO memberDTO) {
        log.info("[CheckEmailName] 시작");

        // 결과를 보관할 boolean flag 변수 생성
        boolean flag = false;

        // MemberDTO 세팅
        memberDTO.setCondition("EMAIL_CONDITION");
        log.info("[CheckEmailName View에서 받아온 값] : {}", memberDTO);

        // 일치하는 값 확인x
        memberDTO = memberService.selectOne(memberDTO);
        log.info("[CheckEmailName selectOne 이후 반환 받은 값] : {}", memberDTO);

        if (memberDTO != null) {
            flag = true;
        }
        return flag+"";
    }

    //비밀번호 변경 확인 로직 => 이름, 이메일
    @PostMapping("/checkPWFind.do")
    public @ResponseBody int checkEmail(@RequestBody MemberDTO memberDTO, Model model) {
        log.info("[CheckEmail] 시작");
        int flag = -1;

        // View에게서 이메일 데이터 받기
        memberDTO.setCondition("PASSWORD_RESET_CONDITION");
        memberDTO = memberService.selectOne(memberDTO);
        log.info("[View에서 받아온 memberDTO] : "+memberDTO);

        // 비밀번호 변경 로직에서 사용하는 조건문
        if (memberDTO != null) {
            flag = memberDTO.getMemberNum();
            model.addAttribute(memberDTO.getMemberNum());
            log.info("[CheckEmail 비밀번호 변경로직 조건문 통과] : "+model);

        }
        log.info("[CheckEmail 종료후 반환되는 값] : " + flag);
        return flag; // 결과 반환
    }

    @PostMapping("/checkNickname.do") // 닉네임 확인 비동기 controller
    public @ResponseBody String checkNickName(MemberDTO memberDTO, Object memberNickname) {
        log.info("[CheckNickname] 시작");
        log.info("[CheckNickname View에서 받은 값 확인] : {}", memberDTO);
        log.info("test memberNickname : {}", memberNickname);
        // 결과를 보관할 boolean flag 변수 생성
        // 기본 값은 false
        boolean flag = false;

        // (C -> M) 해당 닉네임 존재 체크
        memberDTO.setCondition("NICKNAME_CONDITION"); // 나중 수정

        // MemberDAO.selectOne 요청
        // 결과값(MemberDTO) 받아오기
        // memberDTO에 저장
        memberDTO = memberService.selectOne(memberDTO);
        log.info("[CheckNickname selectOne 이후 반환 받은 값] : {}", memberDTO);

        // 만약 사용 가능한 닉네임이라면
        if (memberDTO == null) {
            // flag를 true로 변경
            flag = true;
        }

        // V에게 결과 보내기
        // flag를 String 변수에 담아 반환
        return flag+""; // 결과 반환
    }

}
