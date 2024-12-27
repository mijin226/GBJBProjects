package com.bungeobbang.app.biz.point;

import lombok.Data;

@Data
public class PointDTO {
	private int pointNum;
	private int pointPlus;
	private int pointMinus;
	private String pointContent;
	private int MemberNum;
	
	private String condition;
	private int totalMemberPoint; //회원 잔여 포인트
	
}
