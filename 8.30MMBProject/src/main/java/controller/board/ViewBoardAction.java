package controller.board;

import java.io.IOException;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BoardDAO;
import model.dto.BoardDTO;

// 특정 게시글을 조회하여 board.jsp로 포워딩
public class ViewBoardAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // 파라미터 가져오기 및 유효성 검사
        String boardNumParam = request.getParameter("boardNum");

        if (boardNumParam == null || boardNumParam.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호가 필요.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int bid;
        try {
            bid = Integer.parseInt(boardNumParam);
        } catch (NumberFormatException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 게시글 번호는 Int type 이여야 합니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        BoardDAO boardDAO = new BoardDAO();
        BoardDTO board = new BoardDTO();
        board.setBoardNum(bid);

        try {
            board = boardDAO.selectOne(board);
            if (board != null) {
                request.setAttribute("board", board);
                ActionForward forward = new ActionForward();
                forward.setRedirect(false);
                forward.setPath("board.jsp");
                return forward;
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "404: 게시글을 찾을 수 없습니다.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 게시글 조회 중 서버 오류가 발생.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
