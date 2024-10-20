package controller.page;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardDAO;
import model.dto.BoardDTO;

import java.io.IOException;

// 게시글 수정 페이지(fixboard.jsp)로 포워딩
public class CheckBoardPageAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // boardNum 파라미터 가져오기 및 유효성 검사
        String bidParam = request.getParameter("boardNum");
        if (bidParam == null || bidParam.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호 없음");
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
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호는 Int타입이어야합니다.");
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

        try {
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
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "403: 수정 권한이 없습니다");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            // 게시글을 수정할 권한이 있고, 게시글이 존재할 경우 수정 페이지로 포워딩
            request.setAttribute("boardDTO", boardDTO);
            ActionForward forward = new ActionForward();
            forward.setRedirect(false);
            forward.setPath("fixboard.jsp");
            return forward;

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
