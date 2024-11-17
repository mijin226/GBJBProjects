package com.bungeobbang.app.view.boardController;

import com.bungeobbang.app.biz.board.BoardDTO;
import com.bungeobbang.app.biz.board.BoardService;
import com.bungeobbang.app.biz.boardCate.BoardCateDTO;
import com.bungeobbang.app.biz.boardCate.BoardCateService;
import com.bungeobbang.app.biz.like.LikeDTO;
import com.bungeobbang.app.biz.like.LikeService;
import com.bungeobbang.app.biz.reply.ReplyDTO;
import com.bungeobbang.app.biz.reply.ReplyService;
import com.bungeobbang.app.view.util.FileUtil;
import com.bungeobbang.app.view.util.PaginationUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Controller("board")
@Slf4j
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private BoardCateService boardCateService;
    @Autowired
    private ReplyService replyService;

    //경로
    private final String FOLDER_NAME = "uploads/board/";
    private final String FAIL_URL = "failInfo2"; //실패 처리할 페이지
    private final String FAIL_DO = "redirect:failInfo.do"; //기본 실패 처리

    //session
    private final String SESSION_PK = "userPK"; //세션에 저장된 memberPK

    //한 번에 뜨는 데이터 수 : 페이지네이션 용
    private final int CONTENT_SIZE = 10; // 페이지당 게시글 수

    //msg
    private final String FAIL_BOARD_DELETE_MSG = "게시글을 삭제할 수 없습니다. 관리자에게 문의 바랍니다.";
    private final String FAIL_BOARD_UPDATE_MSG = "게시글을 수정할 수 없습니다. 관리자에게 문의 바랍니다.";
    private final String FAIL_BOARD_INSERT_MSG = "게시글을 작성할 수 없습니다. 관리자에게 문의 바랍니다.";

    //page
    private final String PAGE_BOARD_INFO = "board"; //views 하위 게시글 상세
    private final String PAGE_BOARD_UPDATE = "fixBoard"; //views 하위 게시글 수정
    private final String PAGE_BOARD_ADD = "boardWrite"; //views 하위 게시글 작성
    private final String PAGE_BOARD_LIST = "boardList"; //views 하위 게시글 작성

    //view에서 boardCategoryName으로 전달받는 값 기록용
    private final String PAGE_BOARD_NOTICE = "noticeBoard"; //views 하위 문의게시판
    private final String PAGE_BOARD_COMMU = "boardList"; //views 하위 일반커뮤니티게시판

    //게시글 상세보기
    @RequestMapping("/infoBoard.do")
    public String infoBoard(HttpSession session, Model model, LikeDTO likeDTO, BoardDTO boardDTO, ReplyDTO replyDTO) {
        log.info("log: /infoBoard.do infoBoard - start");
        log.info("log: infoBoard - param likeDTO : [{}]", likeDTO);
        log.info("log: infoBoard - param boardDTO : [{}]", boardDTO);
        boolean userLiked = false; //좋아요 여부 (기본 좋아요 안한 상태)
        Integer sessionMemberPK = (Integer) session.getAttribute(SESSION_PK); //세션 값

        //데이터 조회
        boardDTO.setCondition("ONE_BOARD");
        boardDTO = boardService.selectOne(boardDTO);

        //댓글 정보 조회 (개수)
        replyDTO.setCondition("CNT_BOARD_RP");
        replyDTO = replyService.selectOne(replyDTO);
        int replyCnt = 0;
        if(replyDTO != null) { //NPE방지
            replyCnt = replyDTO.getCnt();
        }

        // 로그인 한 상태라면 좋아요 여부 확인
        if (sessionMemberPK != null) {
            log.info("log: infoBoard - sessionMemberPK : [{}]", sessionMemberPK);
            likeDTO.setMemberNum(sessionMemberPK);
            // 좋아요 상태
            if (likeService.selectOne(likeDTO) != null) {
                log.info("log: infoBoard - likeDTO : true");
                userLiked = true;
            }
        }

        //데이터 전달
        model.addAttribute("board", boardDTO); //게시글 내용 전달
        model.addAttribute("userLiked", userLiked); //초기 좋아요 상태 전달
        model.addAttribute("replyCnt", replyCnt); //댓글 개수만 전달
        //확인
        log.info("log: infoBoard - send board : [{}]", boardDTO);
        log.info("log: infoBoard - param userLiked : [{}]", userLiked);
        log.info("log: infoBoard - param replyCnt : [{}]", replyCnt);
        log.info("log: /infoBoard.do infoBoard - end");
        return PAGE_BOARD_INFO;
    }

    //게시글 삭제
    @RequestMapping("/deleteBoard.do")
    public String deleteBoard(Model model, BoardDTO boardDTO,
                              //상대경로 변환용 realPath 사용을 위한 객체
                              HttpServletRequest request) {
        log.info("log: /deleteBoard.do deleteBoard - start");
        log.info("log: deleteBoard - param boardDTO : [{}]", boardDTO);
        String imagePath; //경로

        //이미지 폴더 삭제 안할 시 필요없는 로직 범위//////////////////////////////////////////////
        //이미지 폴더를 삭제하기 위한 DB 폴더명 조회
        boardDTO.setCondition("ONE_BOARD");
        boardDTO = boardService.selectOne(boardDTO);

        //해당 게시글 이미지 폴더 삭제
        imagePath = request.getServletContext().getRealPath(FOLDER_NAME) + boardDTO.getBoardFolder();  // 이미지 경로 설정
        if(FileUtil.deleteFileAndDirectory(new File(imagePath))){
            log.error("log: deleteBoard - delete file fail check");
        }
        ///////////////////////////////////////////////////////////////////////////////////

        //게시글 삭제
        if (!boardService.delete(boardDTO)) {
            log.error("log: deleteBoard - delete board fail");
            //게시글 삭제 실패
            model.addAttribute("msg", FAIL_BOARD_DELETE_MSG);
            model.addAttribute("path", "loadListBoards.do?boardCategoryName="+boardDTO.getBoardCategoryName());
            return FAIL_URL;
        }

        //해당 카테고리 게시판으로 이동
        log.info("log: /deleteBoard.do deleteBoard - end : loadListBoards.do");
        return "redirect:loadListBoards.do?boardCategoryName="+boardDTO.getBoardCategoryName();
    }

    //게시글 전체 리스트
    @RequestMapping("/loadListBoards.do")
    public String loadListBoards(Model model, Integer page, String boardCategoryName, BoardDTO totalCNT, BoardDTO boardDTO, BoardCateDTO boardCateDTO,
                                 String keyword, String contentFilter, String writeDayFilter, BoardDTO boardTotalCNT) {

        log.info("log: /loadListBoards.do loadListBoards - start");
        log.info("log: loadListBoards - param page : [{}]", page);
        log.info("log: loadListBoards - param boardCategoryName : [{}]", boardCategoryName);
        log.info("log: loadListBoards - param totalCNT : [{}]", totalCNT);
        log.info("log: loadListBoards - param boardDTO : [{}]", boardDTO);
        log.info("log: loadListBoards - param boardCateDTO : [{}]", boardCateDTO);
        log.info("log: loadListBoards - param keyword : [{}]", keyword);
        log.info("log: loadListBoards - param contentFilter : [{}]", contentFilter);

        int totalPage; //총페이지 수 정보
        int totalSize; //게시글 수
        ArrayList<BoardDTO> boardList; //게시글 정보
        int boardCategoryNum = boardCateService.selectOne(boardCateDTO).getBoardCategoryNum(); //해당 카테고리의 번호
        log.info("log: loadListBoards - boardCategoryName : [{}] -> boardCategoryNum : [{}]", boardCateDTO.getBoardCategoryName(), boardCategoryNum);
        //페이지 정보
        page = page == null? 1 : page; //페이지 정보가 있다면 해당 페이지, 없다면 기본값 1

        //검색 세팅 //////////////////////////////////////////////////////////
        // HashMap을 사용하여 검색 조건을 설정
        HashMap<String, String> filterList = new HashMap<>();
        // 검색 조건 설정
        if (keyword != null && !keyword.isEmpty()) {
            log.info("log: loadListBoards - add keyword search : contentFilter [{}]", contentFilter);
            filterList.put(contentFilter, keyword);
        }
        // 작성일 검색 조건 설정
        if (!(writeDayFilter == null || writeDayFilter.equals("ALL"))) { //ALL은 전체기간 검색용
            log.info("log: loadListBoards - add writeDayFilter : [{}]", writeDayFilter);
            filterList.put("SELECT_PART_PERIOD", writeDayFilter);
        }
        //고정 데이터 : 카테고리값
        filterList.put("SELECT_PART_CATE", ""+boardCategoryNum);
        // 검색조건 세팅
        boardDTO.setFilterList(filterList);
        boardTotalCNT.setFilterList(filterList);
        //////////////////////////////////////////////////////////////////////

        //CNT를 구하기위한 DTO
        boardTotalCNT.setCondition("CNT_BOARD");
        boardTotalCNT.setBoardCategoryName(boardCategoryName);
        totalSize = boardService.selectOne(totalCNT).getCnt(); //게시글 수
        log.info("log: loadListBoards - totalSize : [{}]", totalSize);
        // view 에게 보낼 총 페이지 수
        totalPage = PaginationUtils.calTotalPages(totalSize, CONTENT_SIZE);
        log.info("log: loadListBoards - totalPage : [{}]", totalPage);

        //페이지네이션 정보 설정 (startNum, endNum - 기존 Pagination util 재활용)
        PaginationUtils.setPagination(page, CONTENT_SIZE, totalSize, boardDTO);

        //데이터 요청
        boardDTO.setBoardCategoryNum(boardCategoryNum);
        boardDTO.setCondition("FILTER_BOARD");
        boardList = boardService.selectAll(boardDTO);

        //만약 카테고리가 일반 게시판이고 검색이 없을 때만 인기글 데이터 전달 /////////////////////
        if(boardCategoryName.equals(PAGE_BOARD_COMMU) && filterList.size() <= 1){ //카테고리 조건만 filter에 있는 경우는 검색을 안한 경우
            log.info("log: loadListBoards - hotBoardList add");
            //인기글 데이터 요청
            boardDTO.setCondition("HOT_BOARD");
            ArrayList<BoardDTO> hotBoardList = boardService.selectAll(boardDTO);
            model.addAttribute("hotBoardList", hotBoardList);//인기글
            log.info("log: loadListBoards - send hotBoardList : {}", hotBoardList);
        }
        ///////////////////////////////////////////////////////////////////////////////

        //데이터 전달
        model.addAttribute("boardList", boardList); // 게시글 데이터
        model.addAttribute("page", page); // 현재 페이지 번호
        model.addAttribute("totalPage", totalPage); // 게시글 페이지네이션 갯수
        model.addAttribute("boardCategoryName", boardCategoryName); // 게시글 카테고리 이름
        //검색 유지
        model.addAttribute("keyword", keyword); //검색어 유지
        model.addAttribute("contentFilter", contentFilter); //검색종류
        model.addAttribute("writeDayFilter", writeDayFilter); //검색기간
        //확인
        log.info("log: loadListBoards - send boardList : {}", boardList);
        log.info("log: loadListBoards - send page : {}", page);
        log.info("log: loadListBoards - send totalPage : {}", totalPage);
        log.info("log: loadListBoards - send boardCategoryName : [{}]", boardCategoryName);
        log.info("log: loadListBoards - send keyword : [{}]", keyword);
        log.info("log: loadListBoards - send contentFilter : [{}]", contentFilter);
        log.info("log: loadListBoards - send writeDayFilter : [{}]", writeDayFilter);
        log.info("log: /loadListBoards.do loadListBoards - end");
        return PAGE_BOARD_LIST;
    }
    
    //게시글 수정 페이지로 이동
    @RequestMapping(value = "/updateBoard.do", method = RequestMethod.GET)
    public String updateBoard(Model model, BoardDTO boardDTO) {
        log.info("log: /updateBoard.do updateBoard GET - start");
        log.info("log: updateBoard - param boardDTO : [{}]", boardDTO);
        boardDTO.setCondition("ONE_BOARD");
        boardDTO = boardService.selectOne(boardDTO);
        //데이터
        model.addAttribute("board", boardDTO);
        //확인
        log.info("log: updateBoard - send board : [{}]", boardDTO);

        log.info("log: /updateBoard.do updateBoard GET - end");
        return PAGE_BOARD_UPDATE;
    }

    //게시글 작성 페이지 이동
    @RequestMapping(value = "/addBoard.do", method = RequestMethod.GET)
    public String addBoard(Model model, BoardDTO boardDTO) {
        log.info("log: /addBoard.do addBoard GET - start");
        log.info("log: /addBoard GET - param boardDTO : [{}]", boardDTO);
        boardDTO.setBoardFolder(FileUtil.createFileName()); //폴더명 전달
        model.addAttribute("data", boardDTO);
        //확인
        log.info("log: addBoard GET - send data : [{}]", boardDTO);
        log.info("log: /addBoard.do addBoard GET - end : forward");
        return PAGE_BOARD_ADD;
    }

    //게시글 수정
    @RequestMapping(value = "/updateBoard.do", method = RequestMethod.POST)
    public String updateBoard(BoardDTO boardDTO, BoardCateDTO boardCateDTO, Model model) {
        log.info("log: /updateBoard.do updateBoard - start");
        log.info("log: updateBoard - param boardDTO : [{}]", boardDTO);
        boardDTO.setBoardCategoryNum(boardCateService.selectOne(boardCateDTO).getBoardCategoryNum());//카테고리이름을 번호로 변경
        boardDTO.setCondition("BOARD_UPDATE");//컨디션 설정
        if(!boardService.update(boardDTO)){
            log.error("log: updateBoard - update fail");
            model.addAttribute("msg", FAIL_BOARD_UPDATE_MSG);
            //업데이트 실패 시 해당 글 상세보기로 이동
            model.addAttribute("path", "infoBoard.do?BoardNum="+boardDTO.getBoardNum());
            return FAIL_URL;
        }
        //업데이트 성공 시 해당 글 상세보기로 이동
        log.info("log: /updateBoard.do updateBoard - end");
        return "redirect:infoBoard.do?BoardNum="+boardDTO.getBoardNum();
    }

    //게시글 작성
    @RequestMapping(value = "/addBoard.do", method = RequestMethod.POST)
    public String addBoard(BoardDTO boardDTO, BoardCateDTO boardCateDTO, Model model){
        log.info("log: /addBoard.do addBoard - start");
        log.info("log: addBoard - param boardDTO : [{}]", boardDTO);
        log.info("log: addBoard - param boardCateDTO : [{}]", boardCateDTO);

        //카테고리 번호 조회해 저장
        boardDTO.setBoardCategoryNum(boardCateService.selectOne(boardCateDTO).getBoardCategoryNum());
        boardDTO.setCondition("BOARD_INSERT");//컨디션 설정
        //게시글 추가
        if(!boardService.insert(boardDTO)){
            //실패 시
            log.error("log: addBoard - insert fail");
            model.addAttribute("msg", FAIL_BOARD_INSERT_MSG);
            model.addAttribute("path", "loadListBoards.do?boardCategoryName="+boardDTO.getBoardCategoryName());
            return FAIL_URL;
        }
        //작성한 글이 있는 카테고리 페이지로 이동
        log.info("log: /addBoard.do addBoard - end");
        return "redirect:loadListBoards.do?boardCategoryName="+boardDTO.getBoardCategoryName();
    }
}