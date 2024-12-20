package controller.member;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

public class CheckPasswordAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : CheckPasswordAction.java		시작");
		
		System.out.println("	log : CheckPasswordAction.java		사용자 입력 데이터 받아오기");
		// View에게 받을 데이터 비밀번호
		// 비밀번호(password) 데이터 받기
		String password = request.getParameter("password");
		System.out.println("	log : CheckPasswordAction.java		password : " + password);

		// session에서 memberPK 값 받아오기
		HttpSession session = request.getSession();
		int memberPK = (int)session.getAttribute("memberPK");
		System.out.println("	log : CheckPasswordAction.java		session 값 가져오기");
		System.out.println("	log : CheckPasswordAction.java		session(memberPK) : "+ memberPK);

		// MemberDTO memberDTO, DAO memberDAO 객체 new 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// memberDTO에 condition : PASSWORD_CHECK_SELECTONE
		memberDTO.setCondition("PASSWORD_CHECK_SELECTONEE"); // 나중 수정 예상
		System.out.println("	log : CheckPasswordAction.java		condition : "+ memberDTO.getCondition());
		memberDTO.setMemberNum(memberPK);
		memberDTO.setMemberPassword(password);
		System.out.println("	log : CheckPasswordAction.java		memberDTO에 set데이터 완료");

		// MemberDAO.selectOne 요청
		// 결과값(MemberDTO) 받아오기
		// memberDTO에 저장
		memberDTO = memberDAO.selectOne(memberDTO);
		System.out.println("	log : CheckPasswordAction.java		memberDAO.selectOne 요청");
		System.out.println("	log : CheckPasswordAction.java		selectOne 결과 : "+ memberDTO);

		// View에게 meberDTO 전송
		// request.setAttribute에 memberDTO 저장
		request.setAttribute("loginMemberData", memberDTO);
		System.out.println("	log : CheckPasswordAction.java		V에게 selectOne 결과 loginMemberData를 보냄");
		// View에게 실패했을 경우의 값을 boolean으로 줄지 memberDTO로 줄지 물어보기
		
		
		
		// forward 객체 생성
		ActionForward forward = new ActionForward();

		// 이동 방법 : 데이터 전송이므로 redirect(false)
		forward.setRedirect(false);

		// 만약 memberDTO가 존재한다면
		if(memberDTO != null) {
			// 이동 페이지 : mypage.jsp
			forward.setPath("myPage.do");
		}
		// 일치하지 않는다면
		else {
			forward.setPath("checkPWPage.do");
		}

		System.out.println("	log : CheckPasswordAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : CheckPasswordAction.java		종료");
		return forward;
	}

}
