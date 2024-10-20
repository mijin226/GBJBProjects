package controller.page;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProfileUpdatePageAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("	log : ProfileUpdatePageAction.java		시작");
		// forward 객체 생성
		ActionForward forward = new ActionForward();

		// 이동 방법 : 페이지 이동이므로 redirect(true)
		// 이동 페이지 : update_mypage.jsp
		forward.setRedirect(true);
		forward.setPath("update_mypage.jsp");

		System.out.println("	log : ProfileUpdatePageAction.java		forwardPath : "+ forward.getPath());
		System.out.println("	log : ProfileUpdatePageAction.java		종료");
		return forward;
	}
}