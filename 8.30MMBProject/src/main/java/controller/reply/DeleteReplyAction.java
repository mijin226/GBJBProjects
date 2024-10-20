package controller.reply;

import controller.common.Action;
import controller.common.ActionForward;
import model.dao.ReplyDAO;
import model.dto.ReplyDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeleteReplyAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
	    // 1. 세션에서 사용자 정보 가져오기
		HttpSession session = request.getSession();
	    int memberPK = (int) session.getAttribute("memberPK");

        // 2. 요청에서 댓글 ID와 게시글 ID 가져오기
        int replyNum = Integer.parseInt(request.getParameter("replyNum"));
        int bid = Integer.parseInt(request.getParameter("bid"));

        // 3. 댓글 정보 조회를 위해 객체 생성
        ReplyDAO replyDAO = new ReplyDAO();
        ReplyDTO replyDTO = new ReplyDTO();
        
        replyDTO = replyDAO.selectOne(replyDTO);

        ActionForward forward = new ActionForward();
        if (replyDTO != null && replyDTO.getMemberNum() == memberPK) {
            // 4. 작성자가 일치하면 댓글 삭제
            boolean isSuccess = replyDAO.delete(replyDTO);
            if (isSuccess) {
            	// 리다이렉트설정(true);
                forward.setRedirect(true);
                // 게시글이 어디에서 올 건지
                // 게시글 상세보기 페이지랑 댓글 목록이 다르면 view한테 값을 정할지 view와 의논
                forward.setPath("미확정"); // 게시글 상세보기 페이지로 이동
            } else {
                forward.setRedirect(true);
                forward.setPath("reply.do"); 
            }
        } else {
            // 5. 작성자가 일치하지 않거나 댓글이 없으면 에러 페이지로 이동
            forward.setRedirect(true);
            forward.setPath("error.do");
        }
        return forward;
	}
}