package com.bungeobbang.app.biz.declare;

import lombok.Data;

@Data
public class DeclareDTO { //폐점 신고
	private int declareNum; //신고 번호
	private int storeNum; // 가게 번호
	private String declareContent; //신고사유
}
