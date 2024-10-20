package controller.member;

import controller.common.Action;
import controller.common.ActionForward;
import model.dao.MemberDAO;
import model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginAction implements Action{
	// 로그인

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : LoginAction.java		시작");
		
		System.out.println("	log : LoginAction.java		사용자 입력 데이터 받아오기");
		// View에서 이메일(아이디), 비밀번호 데이터 받기
		// String request.getParameter(이메일) (비밀번호)
		String email = request.getParameter("email");
		System.out.println("	log : LoginAction.java		email : " + email);
		String password = request.getParameter("password");
		System.out.println("	log : LoginAction.java		password : " + password);

		// MemberDTO memberDTO, DAO memberDAO 객체 new 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// memberDTO에 condition : LOGIN_SELECTONE 넣어주기
		// memberDTO에 이메일, 비밀번호 넣기
		memberDTO.setCondition("LOGIN_SELECTONE"); // 나중 수정 예상
		System.out.println("	log : LoginAction.java		condition : "+ memberDTO.getCondition());
		memberDTO.setMemberEmail(email);
		memberDTO.setMemberPassword(password);
		System.out.println("	log : LoginAction.java		memberDTO에 set데이터 완료");

		// MemberDAO.selectOne 요청
		// 결과값(MemberDTO) 받아오기
		// memberDTO에 결과값 넣기
		memberDTO = memberDAO.selectOne(memberDTO);
		System.out.println("	log : LoginAction.java		memberDAO.selectOne 요청");
		System.out.println("	log : LoginAction.java		selectOne 결과 : "+ memberDTO);

		// 페이지 이동 (결과에 따라 다른 페이지)
		// forward 객체 생성
		// 이동 방법 : 페이지 이동이므로 (true)
		ActionForward forward = new ActionForward();
		forward.setRedirect(true);

		// 결과값이 있을 때
		// memberDTO 값이 존재할 때(null이 아닐 때)
		if(memberDTO != null) {
			System.out.println("	log : LoginAction.java		로그인 성공");
			// session(memberPK)에 회원번호(PK) 저장
			// session(memberNickName) 회원 닉네임 저장
			// session(memberRole) 회원 권한 저장
			HttpSession session = request.getSession();
			session.setAttribute("memberPK", memberDTO.getMemberNum());
			System.out.println("	log : LoginAction.java		session(memberPK) : " +session.getAttribute("memberPK"));
			session.setAttribute("memberNickName", memberDTO.getMemberNickname());
			System.out.println("	log : LoginAction.java		session(memberNickName) : " +session.getAttribute("memberNickName"));
			session.setAttribute("memberRole", memberDTO.getMemberRole());
			System.out.println("	log : LoginAction.java		session(memberRole) : " +session.getAttribute("memberRole"));

			// 이동할 페이지 : mainPage.do
			forward.setPath("mainPage.do");
		}
		// 로그인이 실패한다면
		else {
			System.out.println("	log : LoginAction.java		로그인 실패");
			// 이동할 페이지 : loginPage.do (로그인)
			forward.setPath("loginPage.do");
		}

		System.out.println("	log : LogoutAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : LoginAction.java		종료");
		return forward;
	}
}