package controller.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import model.dao.MemberDAO;
import model.dto.MemberDTO;

// nicknameChek는 여기로 이동
@WebServlet("/checkNickName")
public class CheckNickNameAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 결과를 보관할 boolean flag 변수 생성
	// 기본 값은 false (중복이 있어 사용불가)
	boolean flag = false;

	public CheckNickNameAction() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 확인 용 로그
		System.out.println("/nicknameCheck");
		System.out.println("	log : CheckNickNameAction.java		시작");
		System.out.println("POST 요청 도착");

		// View에게서 이메일 데이터 받기
		// String request.getParameter(닉네임)
		String nickName = request.getParameter("nickName"); 
		System.out.println("	log : CheckNickNameAction.java		View에서 nickName을 받음");

		// (C -> M) 해당 이메일 존재 체크
		// MemberDTO memberDTO, DAO memberDAO 객체 new 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// memberDTO에 condition : NICKNAME 넣어주기
		// memberDTO에 닉네임 값 넣어주기
		memberDTO.setCondition("NICKNAME_SELECTONE"); // 나중 수정 예상
		memberDTO.setMemberNickname(nickName);
		System.out.println("	log : CheckNickNameAction.java		MemberDTO에 이메일 데이터 넣음");

		// MemberDAO.selectOne 요청
		// 결과값(MemberDTO) 받아오기
		// memberDTO에 저장
		memberDTO = memberDAO.selectOne(memberDTO);
		System.out.println("	log : CheckNickNameAction.java		MemberDAO.selectOne 실행");
		
		// 만약 사용 가능한 닉네임이라면
		if(memberDTO == null) {
			System.out.println("	log : CheckNickNameAction.java		memberDTO가 null");
			// flag를 true로 변경
			flag = true;
		}
		// System.out.println("	log : CheckNickNameAction.java		memberDTO.getMemberNickname : "+ memberDTO.getMemberNickname());

		// 중복 결과 로그
		System.out.println("nickname 중복 결과 : "+ flag);

		// V에게 결과 보내기
		// out 객체를 생성, response.getWriter 호출해 스트림에 텍스트를 저장할 수 있도록 함
		PrintWriter out = response.getWriter();
		// out에 flag값 넣기
		out.print(flag);
		System.out.println("	log : CheckNickNameAction.java		끝");
	}
}