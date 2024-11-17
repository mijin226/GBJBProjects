package com.bungeobbang.app.biz.filterSearch;

public class StoreFilter extends FilterSearchUtil{
	public StoreFilter() {
		super();
		storeFilter();
	}
	
	private void storeFilter() {
		//STORE
		Filter_SQL_MAP.put("NAME_LIKE", "AND STORE_NAME LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("STORE_CLOSED", "AND STORE_CLOSED = ?");
		Filter_SQL_MAP.put("MENU_NORMAL", "AND STORE_MENU_NORMAL = ?");
		Filter_SQL_MAP.put("MENU_VEG", "AND STORE_MENU_VEG = ?");
		Filter_SQL_MAP.put("MENU_MINI", "AND STORE_MENU_MINI = ?");
		Filter_SQL_MAP.put("MENU_POTATO", "AND STORE_MENU_POTATO = ?");
		Filter_SQL_MAP.put("MENU_ICE", "AND STORE_MENU_ICE = ?");
		Filter_SQL_MAP.put("MENU_CHEESE", "AND STORE_MENU_CHEESE = ?");
		Filter_SQL_MAP.put("MENU_PASTRY", "AND STORE_MENU_PASTRY = ?");
		Filter_SQL_MAP.put("MENU_OTHER", "AND STORE_MENU_OTHER = ?");
		Filter_SQL_MAP.put("PAYMENT_CARD", "AND STORE_PAYMENT_CARD = ?");
		Filter_SQL_MAP.put("PAYMENT_CASHMONEY", "AND STORE_PAYMENT_CASHMONEY = ?");
		Filter_SQL_MAP.put("PAYMENT_ACCOUNT", "AND STORE_PAYMENT_ACCOUNT = ?");
		Filter_SQL_MAP.put("STORE_SECRET", "AND STORE_SECRET = ?");	
		Filter_SQL_MAP.put("STORE_ADDRESS", "AND STORE_ADDRESS LIKE concat('%', ? ,'%')");

		Filter_SQL_MAP.put("STORE_DECLARED", "AND STORE_DECLARED = ?");		

		//update
		Filter_SQL_MAP.put("UPDATE_NAME", "STORE_NAME = ?,");
		Filter_SQL_MAP.put("UPDATE_ADDRESS", "STORE_ADDRESS = ?,");
		Filter_SQL_MAP.put("UPDATE_STORE_ADDRESS_DETAIL", "STORE_ADDRESS_DETAIL = ?,");
		Filter_SQL_MAP.put("UPDATE_PHONENUM", "STORE_CONTACT = ?,");
		Filter_SQL_MAP.put("UPDATE_CLOSED", "STORE_CLOSED = ?,");
		Filter_SQL_MAP.put("UPDATE_SECRET", "STORE_SECRET = ?,");
		
	}
}
