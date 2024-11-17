package com.bungeobbang.app.biz.filterSearch;

public class ProductFilter extends FilterSearchUtil{
	
	public ProductFilter() {
		super();
		productFilter();
	}
	
	private void productFilter() {
		//Product
		Filter_SQL_MAP.put("SELECT_PART_CATEGORY","AND PRODUCT_CATEGORY_NUM = ?");
		Filter_SQL_MAP.put("SELECT_PART_NAME", "AND PRODUCT_NAME LIKE CONCAT('%',?,'%')");
		Filter_SQL_MAP.put("SELECT_PART_TITLE", "AND BOARD_TITLE LIKE CONCAT('%',?,'%')");
		Filter_SQL_MAP.put("SELECT_PART_PRICE_MIN", "AND PRODUCT_PRICE >= ?");
		Filter_SQL_MAP.put("SELECT_PART_PRICE_MAX", "AND PRODUCT_PRICE <= ?");	
	}
}