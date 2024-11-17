package com.bungeobbang.app.view.memberController;

import com.bungeobbang.app.biz.member.MemberDTO;
import com.bungeobbang.app.biz.member.MemberService;
import com.bungeobbang.app.biz.point.PointDTO;
import com.bungeobbang.app.biz.point.PointService;
import com.bungeobbang.app.view.util.FileUtil;
import com.bungeobbang.app.view.util.SessionMemberPointUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PointService pointService;

    private final String FAIL_DO = "redirect:failInfo.do"; //기본 실패 처리
    private final String FAIL_URL = "failInfo2"; //실패 처리할 페이지

    //session
    private final String SESSION_ROLE = "userRole";
    private final String SESSION_PK = "userPK";
    private final String SESSION_NICKNAME = "userNickname";
    private final String SESSION_PROFILE = "userProfile";
    private final String SESSION_POINT = "userPoint";

    private final String DEFAULT_IMG = "default_profile.png";//기본 이미지

    //msg
    private final String MSG_FAIL_LOGIN = "올바르지 않은 이메일 또는 비밀번호입니다.";
    private final String MSG_FAIL_PW = "비밀번호 변경에 실패했습니다. 관리자에게 문의바랍니다.";
    private final String MSG_SUCCESS_PW = "비밀번호가 변경되었습니다. 로그인바랍니다.";
    private final String MSG_MEMBER_OUT = "그동안 붕어빵을 이용해 주셔서 감사합니다.";

    @PostMapping("/join.do") // 회원가입 controller
    public String join(HttpServletRequest request, MemberDTO memberDTO, Model model) {
        log.info("[Join] 시작");

        // 프로필 사진 업로드 처리
        String path = "uploads/"; // 업로드할 경로
        String fileName = FileUtil.createFileName(); // 파일 이름 생성
        MultipartFile file = memberDTO.getFile();
        if(file != null || !file.isEmpty()){
            String profilePicPath = FileUtil.insertFile(request.getServletContext(), path, memberDTO.getFile(), fileName);
            log.info("[Join view에서 받은 프로필사진 경로] : {}", profilePicPath);
            memberDTO.setMemberProfileWay(profilePicPath);
        }
        else {
            //입력받은 데이터가 없는 경우 기본 이미지 저장
            memberDTO.setMemberProfileWay(DEFAULT_IMG);
        }
        memberDTO.setMemberRole("USER"); //유저 가입 고정
        memberDTO.setMemberHireDay(LocalDate.now().toString()); //현재 날짜
        // 회원가입 요청
        boolean flag = memberService.insert(memberDTO);
        log.info("[Join insert 성공 실패 여부] : {}", flag);

        // 회원가입 실패 시 파일 삭제
        if (!flag) {
            return FAIL_DO; // 에러 페이지로 이동
        }

        model.addAttribute("msg", "회원가입 성공! 로그인 진행 바랍니다.");
        model.addAttribute("path", "login.do");
        return FAIL_URL;
    }

    @PostMapping(value = "/login.do") // 로그인 controller
    public String login(HttpSession session, MemberDTO memberDTO,Model model) {
        log.info("[Login] 시작");

        // 로그인 로직에 사용할 condition 저장
        memberDTO.setCondition("LOGIN_CONDITON");
        log.info("[Login View에서 전달한 값] : {}", memberDTO);

        memberDTO = memberService.selectOne(memberDTO); // memberService == memberDAO / memberDTO에 selectOne 된 값 저장
        log.info("[Login selectOne된 값] : {}", memberDTO);

        if (memberDTO == null) { // 로그인이 실패한다면
            //실패 안내 후 로그인 페이지로 돌아가게 하기
            model.addAttribute("path", "login.do");
            model.addAttribute("msg", MSG_FAIL_LOGIN);
            return FAIL_URL;
        }

        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute(SESSION_PK, memberDTO.getMemberNum());
        session.setAttribute(SESSION_NICKNAME, memberDTO.getMemberNickname());
        session.setAttribute(SESSION_ROLE, memberDTO.getMemberRole());
        session.setAttribute(SESSION_PROFILE, memberDTO.getMemberProfileWay());

        //포인트 갱신//////////////////////////////////////////////////////////////////////////////////
        PointDTO pointDTO = new PointDTO();
        pointDTO.setMemberNum(memberDTO.getMemberNum());
        pointDTO.setCondition("SELECTONE_MEMBER_POINT");
        pointDTO = pointService.selectOne(pointDTO);
        session.setAttribute(SESSION_POINT, pointDTO.getTotalMemberPoint());
        log.info("log: pointDTO [{}], point [{}]", pointDTO, pointDTO.getTotalMemberPoint());
        //////////////////////////////////////////////////////////////////////////////////////////////

        return "redirect:main.do"; // 메인 페이지로 리다이렉트
    }

    //회원탈퇴
    @PostMapping(value="/deleteMember.do")
    public String deleteMember(HttpSession session, MemberDTO memberDTO, Model model) {
        log.info("[DeleteMember] 시작");
        // View에서 받아올 데이터 없음

        // session에서 회원번호(PK)값 받아옴
        int memberNum = (int)session.getAttribute(SESSION_PK);

        // MemberDTO, DAO 객체 new 생성
        memberDTO.setMemberNum(memberNum);

        // MemberDAO.delete 요청
        // 결과값(boolean flag) 받아오기
        boolean flag = memberService.delete(memberDTO);
        log.info("[DeleteMember 성공 여부 flag] : {}", flag);

        // 계정 삭제 실패 시
        if(!flag) {
            //실패로 이동
            return FAIL_DO;
        }
        //안내 후 로그아웃
        model.addAttribute("msg", MSG_MEMBER_OUT);
        model.addAttribute("path", "logout.do");
        return FAIL_URL;
    }

    @PostMapping(value="/updatePassword.do") // 비빌번호 수정 controller
    public String updatePW(MemberDTO memberDTO, Model model) {
        log.info("[UpdatePW] 시작");
        log.info("[UpdatePW View에서 전달 받은 값] : {}", memberDTO);
        // memberDTO.setCondition : PASSWORD_UPDATE 값 넣어주기
        // memberDTO.set으로 memberNum, password 값 넣기
        memberDTO.setCondition("UPDATE_PASSWORD_CONDTION");

        // memberDAO.update을 사용하여 memberDTO 업데이트
        // boolean flag에 반환값 저장
        boolean flag = memberService.update(memberDTO);
        log.info("[UpdatePW update 수행 이후 반환 값] : {}", flag);
        // 업데이트에 성공했다면
        model.addAttribute("msg", MSG_SUCCESS_PW);
        // flag가 false이면
        if(!flag) {
            model.addAttribute("msg", MSG_FAIL_PW);
        }
        model.addAttribute("path", "login.do");
        return FAIL_URL;

    }
    @GetMapping(value="/deleteMember.do") // 회원 탈퇴 페이지 이동 controller
    public String deleteMember() {
        return "signOut";
    }

    @PostMapping(value="/findPW.do") // 비밀번호 찾기 페이지 이동 controller
    public String findPW() {
        // 이동 페이지 : findPw.jsp
        return "redirect:findPw.jsp";
    }

    @GetMapping(value="/addMember.do") // 회원 가입 페이지 이동 controller
    public String joinPage() {
        return "redirect:signup.jsp";
    }

    @RequestMapping(value="/login.do") // 로그인 페이지 이동 controller
    public String loginiPage() {
        return "redirect:login.jsp";
    }

    @GetMapping(value="/findPW.do") // 비밀번호 변경 모달에서 사용하는 controller
    public String updatePW() {
        // 모달에서 findPW.jsp로 이동
        return "redirect:findPW.jsp";
    }

    @RequestMapping(value="/logout.do") // 로그아웃 controller
    public String logout (HttpSession session) {
        //세션 정보 삭제
        session.removeAttribute(SESSION_PK);
        session.removeAttribute(SESSION_NICKNAME);
        session.removeAttribute(SESSION_ROLE);
        session.removeAttribute(SESSION_PROFILE);
        session.removeAttribute(SESSION_POINT);
        return "redirect:main.do"; // main.do 요청으로 이동
    }

    @GetMapping(value = "/signupPage.do")
    public String signup() {
        return "redirect:signUp.jsp";
    }
}
