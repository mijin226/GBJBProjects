package controller.board;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardDAO;
import model.dto.BoardDTO;

import java.io.IOException;

// 특정 게시글을 삭제합니다. 게시글 작성자만 삭제 가능
public class DeleteBoardAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // bid 파라미터 가져오기 및 유효성 검사
        String bidParam = request.getParameter("bid");
        if (bidParam == null || bidParam.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호 필요");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int bid;
        try {
            bid = Integer.parseInt(bidParam);
        } catch (NumberFormatException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호는 Int 타입이여야 합니다");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        HttpSession session = request.getSession();
        Integer writerPK = (Integer) session.getAttribute("memberPK");
        if (writerPK == null) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "401: 인증되지 않은 사용자");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        BoardDAO boardDAO = new BoardDAO();
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNum(bid);

        boardDTO = boardDAO.selectOne(boardDTO);

        if (boardDTO == null) {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "404: 게시글을 찾을 수 없습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if (boardDTO.getMemberNum() != writerPK) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "403: 삭제 권한이 없습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        try {
            boardDTO.setCondition(""); // delete 요청 시 혹여나 영향을 주는 것을 방지
            boolean result = boardDAO.delete(boardDTO);
            if (result) {
                ActionForward forward = new ActionForward();
                forward.setRedirect(true);
                forward.setPath("boardList.jsp");
                return forward;
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 게시글 삭제 실패");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 서버 오류");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
