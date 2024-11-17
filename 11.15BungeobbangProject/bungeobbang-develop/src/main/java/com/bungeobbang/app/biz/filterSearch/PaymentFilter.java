package com.bungeobbang.app.biz.filterSearch;

public class PaymentFilter extends FilterSearchUtil{
	public PaymentFilter() {
		super();
		paymentFilter();
	}

	private void paymentFilter() {
		// PAYMENT 
		// 관리자 확인여부
		Filter_SQL_MAP.put("ADMIN_CHECKED", "AND ADMIN_CHECKED =?");
		// 회원 번호
		Filter_SQL_MAP.put("MEMBER_NUM", "AND MEMBER_NUM =?");
	}
}
