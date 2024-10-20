package controller.board;

import java.io.IOException;
import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BoardDAO;
import model.dto.BoardDTO;

public class SearchBoardsAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		// 1. 사용자 입력 값(검색 기간, 검색 키워드, 카테고리)을 요청 객체에서 가져옵니다.
//		String searchDateRange = request.getParameter("searchDateRange");
		String searchKeyword = request.getParameter("searchKeyword");
		String searchContent = request.getParameter("searchContent");
		String categoryParam = request.getParameter("category");

		// 2. 검색 키워드 유효성 검사
		if ( searchKeyword == null || searchKeyword.isEmpty() || 
			 searchContent == null || searchContent.isEmpty() || 
			 categoryParam == null || categoryParam.isEmpty() ) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 검색 키워드가 필요합니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		int category = 0; // 기본값 설정
		if (categoryParam != null && !categoryParam.isEmpty()) {
			try {
				category = Integer.parseInt(categoryParam);
			} catch (NumberFormatException e) {
				try {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400:카테고리는 Int 타입이여야 합니다.");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return null;
			}
		}

		// 3. 검색을 수행하기 위해 DAO와 DTO 객체를 생성합니다.
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO boardDTO = new BoardDTO();

		// 4. 검색 키워드에 따라 검색 조건을 설정합니다.
		if (searchKeyword.equals("searchTitle")) {
			boardDTO.setCondition("TITLE_SELECTALL");  // 제목으로 검색
		} else if (searchKeyword.equals("searchWriter")) {
			boardDTO.setCondition("WRITER_SELECTALL");  // 작성자로 검색
		} else if (searchKeyword.equals("searchContents")) {
			boardDTO.setCondition("CONTENTS_SELECTALL");  // 내용으로 검색
		} else {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400: 잘못된 검색 키워드입니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		// 5. 카테고리를 DTO에 설정합니다.
//		boardDTO.setSearchDateRange(searchDateRange);
		boardDTO.setCategoryNum(category);
		boardDTO.setKeyword(searchContent);  // 사용자가 입력한 검색어를 설정

		// 6. DAO를 사용하여 조건에 맞는 게시글 목록을 조회합니다.
		ArrayList<BoardDTO> searchResults = null;
		try {
			searchResults = boardDAO.selectAll(boardDTO);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: 게시글 검색 중 서버 오류.");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		// 7. 검색 결과가 없는 경우 처리
		if (searchResults == null || searchResults.isEmpty()) {
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "404: 검색 결과를 찾을 수 없습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		// 7. 조회된 게시글 목록을 요청 객체에 설정하여 JSP에서 사용할 수 있도록 합니다.
		request.setAttribute("boardList", searchResults);

		// 8. 검색 결과를 보여줄 JSP 페이지로 포워딩합니다.
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);  // 요청 데이터를 유지하며 포워딩
		forward.setPath("boardList.jsp");

		return forward;
	}
}
