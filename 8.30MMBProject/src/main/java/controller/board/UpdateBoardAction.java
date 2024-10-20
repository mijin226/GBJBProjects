package controller.board;

import java.io.IOException;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardDAO;
import model.dto.BoardDTO;

public class UpdateBoardAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        // 파라미터 가져오기 및 유효성 검사
        String bidParam = request.getParameter("bid");
        String fixBoardTitle = request.getParameter("fixBoardTitle");
        String fixBoardContent = request.getParameter("fixBoardContent");
        String secretBoardContents = request.getParameter("secretBoardContents");
        String categoryParam = request.getParameter("category");

        if (bidParam == null || bidParam.isEmpty() || 
            fixBoardTitle == null || fixBoardTitle.isEmpty() ||
            fixBoardContent == null || fixBoardContent.isEmpty() ||
            categoryParam == null || categoryParam.isEmpty()) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 필수 입력 값이 누락되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int bid;
        int category;
        try {
            bid = Integer.parseInt(bidParam);
            category= Integer.parseInt(categoryParam);
        } catch (NumberFormatException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 잘못된 요청입니다: 게시글 번호는 숫자여야 합니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        HttpSession session = request.getSession();
        Integer writerPK = (Integer) session.getAttribute("memberPK");
        if (writerPK == null) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "401: 인증되지 않은 사용자입니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        BoardDAO boardDAO = new BoardDAO();
        BoardDTO boardUpdate = new BoardDTO();

        boardUpdate.setBoardNum(bid);
        boardUpdate.setTitle(fixBoardTitle);
        boardUpdate.setContent(fixBoardContent);
        boardUpdate.setMemberNum(writerPK);
        boardUpdate.setCategoryNum(category);

        // 비밀글/공개글 여부 설정
        if (secretBoardContents != null && secretBoardContents.equals("on")) {
            boardUpdate.setVisibility("비공개");  // 비밀글로 설정
        } else {
            boardUpdate.setVisibility("공개");  // 공개글로 설정
        }

        boolean result;
        try {
            result = boardDAO.update(boardUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 게시글 수정 중 서버 오류가 발생.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        if (result) {
            // Update 성공 시, 해당 글 다시 조회하여 board.jsp로 포워딩
            BoardDTO updatedBoard;
            try {
                updatedBoard = boardDAO.selectOne(boardUpdate);

                if (updatedBoard != null) {
                    request.setAttribute("board", updatedBoard);
                    ActionForward forward = new ActionForward();
                    forward.setRedirect(false);  // 포워딩 방식으로 이동
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
        } else {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 게시글 수정에 실패.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
