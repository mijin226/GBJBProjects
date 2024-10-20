package controller.reply;

import java.io.IOException;
import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.ReplyDAO;
import model.dto.ReplyDTO;

// 특정 게시글의 모든 댓글을 조회하는 액션 클래스
public class ListReplyAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // 1. 게시글 번호(boardNum)를 파라미터로 가져오기 및 유효성 검사
        String bid = request.getParameter("boardNum");

        if (bid == null || bid.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다: 게시글 번호가 필요합니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int boardNum;
        try {
            boardNum = Integer.parseInt(bid);
        } catch (NumberFormatException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다: 게시글 번호는 숫자여야 합니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        // 2. 댓글을 조회하기 위해 DAO와 DTO 객체를 생성합니다.
        ReplyDAO replyDAO = new ReplyDAO();
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setBoardNum(boardNum);  // 조회할 게시글 번호 설정

        // 3. DAO를 사용하여 해당 게시글의 모든 댓글을 조회합니다.
        ArrayList<ReplyDTO> replyList = null;
        try {
            replyList = replyDAO.selectAll(replyDTO);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 조회 중 서버 오류가 발생했습니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        // 4. 댓글 조회 결과가 없는 경우 처리
        if (replyList == null || replyList.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "댓글을 찾을 수 없습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // 5. 조회된 댓글 목록을 요청 객체에 설정하여 JSP에서 사용할 수 있도록 합니다.
        request.setAttribute("replyList", replyList);

        // 6. 댓글 목록을 보여줄 JSP 페이지로 포워딩합니다.
        ActionForward forward = new ActionForward();
        forward.setRedirect(false);  // 요청 데이터를 유지하며 포워딩
        forward.setPath("replyList.jsp");

        return forward;
    }
}
