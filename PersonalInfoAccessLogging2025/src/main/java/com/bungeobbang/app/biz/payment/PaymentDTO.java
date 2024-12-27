package com.bungeobbang.app.biz.payment;

import java.util.HashMap;

import lombok.Data;

@Data
public class PaymentDTO {
	private int paymentNum; //PK
	private int memberNum; // 회원 번호(외래키)
	private int paymentAmount; // 결제 금액
	private String adminChecked; //관리자 확인
	
	private String paymentAt; //결제 시간
	private String paymentName;  //결제 상품명
	private String memberEmail;	//회원 EMAIL
	private String impUUid;  //포트원에서 부여하는 상점 고유번호
	
	//개발용
	private String condition;
	private HashMap<String, String> filterList; //필터(키 값, 사용자 입력값)
	private int startNum;
	private int endNum;

}
