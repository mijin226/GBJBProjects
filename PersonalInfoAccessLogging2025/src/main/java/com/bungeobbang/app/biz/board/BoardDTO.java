package com.bungeobbang.app.biz.board;

import java.util.HashMap;

import lombok.Data;

@Data
public class BoardDTO {
	private int boardNum;				//게시글 번호
	private String boardTitle;			//제목
	private String boardContent;		//내용
	private String boardFolder; 		//게시글 폴더명
	private int memberNum;				//작성자
	private String boardWriteDay;		//작성일자
	private String boardOpen;			//비공개 공개 여부 Y/N
	private int boardCategoryNum;			//게시글 카테고리 번호
	private int storeNum;				//가게 번호
	private String boardDelete;			//관리자 글 삭제 여부 Y/N
	
	//타 테이블
	//좋아요 합계
	private int likeCnt;				//좋아요 수(합계)
	private int replyCnt; //댓글 수
	//멤버
	private String memberNickname;		//닉네임
	private String memberProfileWay; 	//회원 프로필 사진 경로
	//카테고리
	private String boardCategoryName;		//게시글 카테고리 명
	private int productNum;
	
	//개발용
	private String condition;			//컨디션
	private HashMap<String, String> filterList; //필터검색용 <검색구분용, 검색값>
	private int startNum;				//페이지네이션 시작번호
	private int endNum;					//페이지네이션 끝번호
	private int cnt;					//페이지네이션 개수 반환용
	private int maxPk;					//가장 최근에 사용된 PK번호	

}
