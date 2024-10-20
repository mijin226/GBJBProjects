package controller.page;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import model.dao.MemberDAO;
import model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewMemberListPageAction implements Action{
	// 신규회원목록 출력

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : NewMemberAction.java		시작");
		// 데이터를 전달해줄 ArrayList<MemberDTO> datas 객체 생성
		ArrayList<MemberDTO> datas = new ArrayList<MemberDTO>();

		// MemberDTO, MemberDAO 객체 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// MemberDTO에 condition 입력 : NEWMEMBERS
		memberDTO.setCondition("NEWMEMBERS_SELECTALL");
		System.out.println("	log : NewMemberAction.java		condition : "+ memberDTO.getCondition());

		// MemberDAO.selectAll로 신규회원 목록 받아오기
		// datas 리스트에 받기
		datas = memberDAO.selectAll(memberDTO);
		System.out.println("	log : NewMemberAction.java		memberDAO.selectAll 요청");
		System.out.println("	log : NewMemberAction.java		selectAll 결과 : "+ memberDTO);

		// View에게 전달
		// request.setAttribute에 datas저장
		request.setAttribute("newMember", datas);
		System.out.println("	log : NewMemberAction.java		V에게 selectAll 결과 보냄");

		// 페이지 이동
		// forward 객체 생성
		ActionForward forward = new ActionForward();
		// 이동 방법 : 데이터가 있으므로 forward (false)
		// 이동 페이지 : newMember.jsp
		forward.setRedirect(false);
		forward.setPath("newMember.jsp");
		
		System.out.println("	log : NewMemberAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : NewMemberAction.java		종료");
		return forward;
	}
}