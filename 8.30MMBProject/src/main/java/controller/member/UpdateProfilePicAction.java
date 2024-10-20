package controller.member;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import controller.common.Action;
import controller.common.ActionForward;
import controller.common.ProfilePicUpload;
import model.dao.MemberDAO;
import model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class UpdateProfilePicAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : UpdateProfilePicAction.java		시작");

		// 프로필사진을 담을 변수 선언
		String profilePic = null;
		// 이전 프로필사진을 담을 변수 선언
		// 업데이트 성공시 파일 삭제를 위해 파일명을 복사해놓음
		String preProfilePic = null;

		// session(memberPK)에서 회원번호(PK)값 가져오기
		HttpSession session = request.getSession();
		int memberPK = (int) session.getAttribute("memberPK");
		System.out.println("	log : UpdateProfilePicAction.java		session(memberPK) : "+ memberPK);

		// MemberDTO memberDTO, DAO memberDAO 객체 new 생성
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// memberDTO에 condition : PROFILEWAY_SELECTONE 넣어주기
		memberDTO.setCondition("PROFILEWAY_SELECTONE"); // 나중 수정 예상, 프로필 사진
		System.out.println("	log : UpdateProfilePicAction.java		condition : "+ memberDTO.getCondition());
		// memberDTO에 회원번호(PK)값 넣어주기
		memberDTO.setMemberNum(memberPK);
		System.out.println("	log : UpdateProfilePicAction.java		memberDTO에 set데이터 완료");

		// 기존의 파일을 지우기 위해 기존 데이터 불러오기
		// memberDAO.selectOne
		memberDTO = memberDAO.selectOne(memberDTO);
		System.out.println("	log : UpdateProfilePicAction.java		memberDAO.selectOne 요청");
		System.out.println("	log : UpdateProfilePicAction.java		selectOne 결과 : "+ memberDTO);

		// 만약 memberDTO가 존재하지 않는다면
		if(memberDTO == null) {
			// 기존 데이터가 존재하지 않게 됨
			// 에러 페이지 표시
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "404: 데이터가 존재하지 않는 오류가 생김");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 입력된 파일을 확인하고 가져오는 메서드
		profilePic = ProfilePicUpload.profilePicUpload(request, response);
		System.out.println("	log : JoinAction.java		profilePic : "+ profilePic);

		// memberDTO에 condition : PROFILEPIC_UPDATE 넣어주기
		memberDTO.setCondition("PROFILEPIC_UPDATE"); // 나중 수정 예상
		// memberDTO에 변경된 프로필사진주소 값 넣어주기
		memberDTO.setMemberProfileWay(profilePic);

		// MemberDAO.update 요청
		// 결과값(boolean flag) 받아오기
		boolean flag = memberDAO.update(memberDTO);
		System.out.println("	log : UpdateProfilePicAction.java		memberDAO.update 요청");
		System.out.println("	log : UpdateProfilePicAction.java		update 결과 flag : "+ flag);

		// View에서 결과 출력용
		// request.setAttribute에 flag 저장
		request.setAttribute("result", flag);
		System.out.println("	log : UpdateProfilePicAction.java		V에게 update 결과 result를 보냄");

		// 페이지 이동 (결과에 따라 다른 페이지)
		// forward 객체 생성
		// 이동 방법 : 전송할 데이터가 있으므로 forward(false)
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);

		// update가 실패한다면
		// 만약 flag가 false라면
		if(!flag) {
			System.out.println("	log : UpdateProfilePicAction.java		update 실패");

			// 기본 프로필 사진이 아니면 삭제하는 메서드
			ProfilePicUpload.deletefile(profilePic);
		}
		// 업데이트 성공 실패에 상관없이 수정 페이지로 이동
		// 이동할 페이지 : update_profilePage.do
		forward.setPath("update_profilePage.do");

		System.out.println("	log : UpdateProfilePicAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : UpdateProfilePicAction.java		종료");
		return forward;
	}
}
