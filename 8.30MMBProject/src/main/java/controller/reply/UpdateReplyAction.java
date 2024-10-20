package controller.reply;

import java.io.IOException;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ReplyDAO;
import model.dto.ReplyDTO;

// 댓글 수정 기능을 담당하는 액션 클래스
public class UpdateReplyAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // 파라미터 가져오기 및 유효성 검사
        String replyNumParam = request.getParameter("replyNum");
        String updatedContent = request.getParameter("updatedContent");

        if (replyNumParam == null || replyNumParam.isEmpty() || updatedContent == null || updatedContent.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 입력 값이 누락되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int replyNum;
        try {
            replyNum = Integer.parseInt(replyNumParam);
        } catch (NumberFormatException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다: 댓글 번호는 숫자여야 합니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        HttpSession session = request.getSession();
        Integer memberPK = (Integer) session.getAttribute("memberPK");
        if (memberPK == null) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 사용자입니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        ReplyDAO replyDAO = new ReplyDAO();
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyNum(replyNum);
        replyDTO.setMemberNum(memberPK);

        // 댓글이 존재하고 작성자가 본인인지 확인하는 작업
        try {
            ReplyDTO existingReply = replyDAO.selectOne(replyDTO);
            if (existingReply == null || existingReply.getMemberNum()!=memberPK) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "댓글 수정 권한이 없습니다.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 확인 중 서버 오류가 발생했습니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        // 댓글 수정 요청
        replyDTO.setReplyContent(updatedContent);
        boolean result;
        try {
            result = replyDAO.update(replyDTO);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 수정 중 서버 오류가 발생했습니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        if (result) {
            // 수정 성공 시, 댓글의 글(FK)로 리다이렉트
            try {
                int boardNum = replyDTO.getBoardNum();
                ActionForward forward = new ActionForward();
                forward.setRedirect(true);
                forward.setPath("board.jsp?boardNum=" + boardNum);  // 수정된 댓글이 포함된 게시글로 이동
                return forward;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 조회 중 서버 오류가 발생했습니다.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        } else {
            // 수정 실패 시, 이전 페이지로 돌아가기
            try {
                response.getWriter().print("<script>history.back();</script>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
