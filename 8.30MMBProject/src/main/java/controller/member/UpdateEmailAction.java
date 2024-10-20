package controller.member;

import controller.common.Action;
import controller.common.ActionForward;
import model.dao.MemberDAO;
import model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateEmailAction implements Action{
	// 이메일 변경

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : UpadtaeEmailAction.java		시작");
		
		// V에서 이메일 값 받아오기
		String email = request.getParameter("email");
		System.out.println("	log : UpadtaeEmailAction.java		email : " + email);
		
		// session(memberPK)에서 회원번호(PK)값 가져오기
		HttpSession session = request.getSession();
		int memberPK = (int) session.getAttribute("memberPK");
		System.out.println("	log : UpadtaeEmailAction.java		session(memberPK) : "+ memberPK);

		// MemberDTO memberDTO, DAO memberDAO 객체 new 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// memberDTO에 condition : EMAIL_UPDATE 넣어주기
		// memberDTO에 회원번호(PK)값 넣어주기
		// memberDTO에 이메일 값 넣어주기
		memberDTO.setCondition("EMAIL_UPDATE"); // 나중 수정 예상
		System.out.println("	log : UpadtaeEmailAction.java		condition : "+ memberDTO.getCondition());
		memberDTO.setMemberNum(memberPK);
		memberDTO.setMemberEmail(email);
		System.out.println("	log : UpadtaeEmailAction.java		memberDTO에 set데이터 완료");

		// MemberDAO.update 요청
		// 결과값(boolean flag) 받아오기
		boolean flag = memberDAO.update(memberDTO);
		System.out.println("	log : UpadtaeEmailAction.java		memberDAO.update 요청");
		System.out.println("	log : UpadtaeEmailAction.java		update 결과 flag : "+ flag);

		// View에서 결과 출력용
		// request.setAttribute에 flag 저장
		request.setAttribute("result", flag);
		System.out.println("	log : UpadtaeEmailAction.java		V에게 update 결과 result를 보냄");

		// 페이지 이동 (결과에 따라 다른 페이지)
		// forward 객체 생성
		// 이동 방법 : 전송할 데이터가 있으므로 forward(false)
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);

		// 업데이트 성공 실패에 상관없이 수정 페이지로 이동
		// 이동할 페이지 : update_profilePage.do
		forward.setPath("update_profilePage.do");
		
		System.out.println("	log : UpadtaeEmailAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : UpadtaeEmailAction.java		종료");
		return forward;
	}
}