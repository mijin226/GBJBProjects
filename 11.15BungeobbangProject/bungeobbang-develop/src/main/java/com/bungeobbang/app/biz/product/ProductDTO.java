package com.bungeobbang.app.biz.product;

import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductDTO {
	private int productNum;				//상품번호
	private String productName;			//이름
	private int productPrice;			//가격
	private String productProfileWay;	//썸네일 이미지
	private int productCategoryNum;			//카테고리 번호
	
	//타 테이블
	//게시글 (board)
	private int boardNum;
	private String boardTitle;			//제목
	private String boardContent;		//내용
	//상품 카테고리
	private String productCategoryName;		//카테고리명
	
	//개발용
	private String condition;			//컨디션
	private HashMap<String, String> filterList; //필터검색용 <검색구분용, 검색값>
	private MultipartFile file;
	private int startNum;				//페이지네이션 시작번호
	private int endNum;					//페이지네이션 끝번호
	private int cnt;					//페이지네이션 개수 반환용

}