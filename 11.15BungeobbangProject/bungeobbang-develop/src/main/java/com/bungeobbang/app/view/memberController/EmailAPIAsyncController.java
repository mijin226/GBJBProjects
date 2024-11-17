package com.bungeobbang.app.view.memberController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.bungeobbang.app.biz.member.MemberDTO;
import com.bungeobbang.app.biz.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@Slf4j
@RestController
public class EmailAPIAsyncController { // 비밀번호 찾기에 사용되는 비동기 controller

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private JavaMailSender mailSender; // JavaMailSender 주입

    

    
//    @PostMapping(value="/checkEmailName.do") // 이메일 이름 비동기 확인 controller
//    public @ResponseBody Map<String, Object> emailNameCheck(MemberDTO memberDTO) {
//        log.info("[CheckEmailName] 시작");
//
//        // 결과를 보관할 boolean flag 변수 생성
//    	Map<String, Object> result = new HashMap<>();
//        boolean flag = false;
//
//        // MemberDTO 세팅
//        memberDTO.setCondition("EMAIL_NAME_SELECTONE");
//        log.info("[CheckEmailName View에서 받아온 값] : {}", memberDTO);
//
//        // 일치하는 값 확인x
//        memberDTO = memberService.selectOne(memberDTO);
//        log.info("[CheckEmailName selectOne 이후 반환 받은 값] : {}", memberDTO);
//        if (memberDTO != null) {
//            flag = true;
//        }
//
//        result.put("flag", flag);
//        result.put("memberDTO", memberDTO);
//        log.info("[CheckEmailName 반환 해줄 return값 확인] : {}", result);
//
//        return result;
//    }
    
    @PostMapping("/checkEmailNum.do") // 이메일로 전송된 인증번호 비동기 확인 controller
    public @ResponseBody boolean emailNumCheck(@RequestParam("checkNum") String inputCheckNum, HttpSession session) {
        log.info("[CheckEmailNum] 시작");

        // session에 있는 원본 인증값 가져오기
        String checkNum = (String) session.getAttribute("checkNum");
        log.info("[CheckEmailNum session에 저장된 인증 번호] : {}", checkNum);

        // 만약 원본 인증값과 V의 입력값이 같다면
        boolean flag = inputCheckNum.equals(checkNum);
        log.info("[CheckEmmailNum 인증 flag 여부] : {}", flag);

        return flag; // true 또는 false 반환
    }
    
    @PostMapping("/sendEmail.do") // 이메일 전송(인증번호) 비동기 controller
    public @ResponseBody boolean sendEmail(@RequestParam("email") String receiveEmail, HttpSession session) {
        log.info("[SendEmail] 시작");

        // 랜덤한 인증번호 4자리 생성
        Random rand = new Random();
        String checkNum = "";

        for (int i = 1; i <= 4; i++) {
            checkNum += rand.nextInt(10);
        }
        log.info("[SendEmail 생성한 인증번호] : {}", checkNum);

        // 이메일 제목 및 내용 설정
        String title = "갈빵질빵에서 보낸 인증번호 메일입니다.";
        String content = "인증번호 \n" + checkNum;

        // 이메일 송신
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("galbbangjilbbang@naver.com"); // 발신자 설정
            message.setTo(receiveEmail); // 수신자 설정
            message.setSubject(title); // 제목 설정
            message.setText(content); // 내용 설정

            mailSender.send(message); // 이메일 전송
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 전송 실패 시 false 반환
        }

        // 인증번호를 비교하기 위해 session에 저장
        session.setMaxInactiveInterval(180); // 세션의 유효 시간을 3분으로 설정 (180초)
        
        // 남은 시간 계산
        long remainTime = System.currentTimeMillis() + (180 * 1000); // 현재시간 + 180초
        session.setAttribute("checkNum", checkNum); // 세션에 인증번호 저장
        session.setAttribute("remainTime", remainTime); // 세션에 남은 시간 저장

        return true; // 전송 성공 시 true 반환
    }
}
