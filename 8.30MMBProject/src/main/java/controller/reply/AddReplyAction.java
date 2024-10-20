package controller.reply;

import controller.common.Action;
import controller.common.ActionForward;
import model.dao.ReplyDAO;
import model.dto.ReplyDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddReplyAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		//1. 세션에서 사용자 정보 가져오기
		//1-1. 세션불러와서 작성자 PK 가져오기
		HttpSession session = request.getSession();
		int memberPK = (int) session.getAttribute("memberPK");
		
		//2. View에서 요청 파라미터 값으로 
		//2-1. 댓글내용 newReplyContents 
		//2-2. 글번호 bid
		String content = request.getParameter("newReplyContents");
		int bid = Integer.parseInt(request.getParameter("bid"));
		
		//3. 댓글 객체 생성 및 설정
		ReplyDTO replyDTO = new ReplyDTO();
        ReplyDAO replyDAO = new ReplyDAO();
        replyDTO.setMemberNum(memberPK);
        replyDTO.setReplyContent(content);
        replyDTO.setBoardNum(bid);
        
        //4. 댓글을 DB에 추가
        boolean flag = replyDAO.insert(replyDTO);
        
        //5. 결과에 따라 포워딩 처리
        ActionForward forward = new ActionForward();
        if (flag) {
            forward.setRedirect(true);
            forward.setPath("reply.jsp"); // 게시글 상세보기 페이지로 이동
        } else {
            forward.setRedirect(true);
            forward.setPath("history go back -1"); // 에러 페이지로 이동
        }
        return forward;
	}
}