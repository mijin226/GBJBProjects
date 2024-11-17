package com.bungeobbang.app.biz.member;

import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MemberDTO { 
	
	private int memberNum;				//멤버번호
	private String memberEmail;			//이메일
	private String memberPassword;		//비밀번호
	private String memberName;			//이름
	private String memberNickname;		//닉네임
	private String memberPhone;			//전화번호
	private String memberProfileWay;	//프로필이미지경로
	private String memberRole;			//권한
	private String memberHireDay;		//가입일자
	
	private MultipartFile file; 
	//개발용
	private String condition;			//컨디션
	private HashMap<String, String> filterList; //필터검색용 <검색구분용, 검색값>
	private int startNum;				//페이지네이션 시작번호
	private int endNum;					//페이지네이션 끝번호
	private int cnt;					//페이지네이션 개수 반환용

}
