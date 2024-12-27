package com.bungeobbang.app.view.memberController;

import com.bungeobbang.app.biz.member.MemberDTO;
import com.bungeobbang.app.biz.member.MemberService;
import com.bungeobbang.app.biz.point.PointDTO;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class CheckPasswordController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ServletContext application; // ServletContext를 주입받아 사용

	// 마이 페이지 들어가기전 비밀번호 확인 이후 role 값에 따라 마이페이지 이동을 다르게 해주는 controller
	@PostMapping(value="/checkMyPagePW.do")
	public String checkPassword(HttpSession session, MemberDTO memberDTO, PointDTO pointDTO, Model model) {
		log.info("[CheckMypagePW] 시작");
		// View에게 받을 데이터 비밀번호
		// 비밀번호(password) 데이터 받기

		// session에서 memberPK 값 받아오기
		Integer memberNum = (Integer) session.getAttribute("userPK");
		if(memberNum == null){
			return "redirect:/login.do";
		}

		// memberDTO에 condition : PASSWORD_CHECK_SELECTONE
		memberDTO.setCondition("PASSWORD_CHECK_CONDITION"); // 나중 수정 예상
		memberDTO.setMemberNum(memberNum);
		log.info("[CheckMypagePW View에서 전달해준 값] : "+memberDTO);

		// MemberDAO.selectOne 요청
		memberDTO = memberService.selectOne(memberDTO);


		// 만약 memberDTO가 존재한다면
		if(memberDTO != null) {
			log.info("[CheckMypagePW selectOne 이후 memberDTO가 존재 할 때 실행되는 로그]");
			memberDTO.setCondition("INFO_CONDITION");
			memberDTO = memberService.selectOne(memberDTO);
			log.info("[CheckMypagePW 비빌번호 확인 이후 사용자 정보 반환] : "+memberDTO);
			// session(role) 값 가져오기
			String role = (String)session.getAttribute("userRole");

			// 만약 사용자가 관리자라면
			if(role.equals("admin")) {
				model.addAttribute("memberDTO", memberDTO);
				return "adminPage";
			}
			//  그 외의 사용자라면
			else {
				// 포인트를 애플리케이션 속성에서 가져오기
				Integer applicationPoint = (Integer) application.getAttribute("applicationPoint");
				model.addAttribute("applicationPoint", applicationPoint);
				model.addAttribute("memberDTO", memberDTO);
				log.info("[CheckMyPagePW 어플리케이션에 있는 memberPoint] : {}", applicationPoint);
				return "myPage";
			}
		}
		// 일치하지 않는다면
		else {
			return "redirect:main.do";
		}
	}
}
