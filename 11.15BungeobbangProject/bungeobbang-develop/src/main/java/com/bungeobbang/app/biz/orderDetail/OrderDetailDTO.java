package com.bungeobbang.app.biz.orderDetail;

import lombok.Data;

@Data
public class OrderDetailDTO {
	private int orderDetailNum;	//주문 상세 정보
	private int productNum;	//상품 번호(외래키)
	private int orderQuantity;	//상품 수량
	private int orderNum;	// 주문 번호
}
