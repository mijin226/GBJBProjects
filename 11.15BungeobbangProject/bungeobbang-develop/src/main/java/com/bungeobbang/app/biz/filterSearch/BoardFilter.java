package com.bungeobbang.app.biz.filterSearch;

public class BoardFilter extends FilterSearchUtil{
	public BoardFilter() {
		super();
		boardFilter();
	}
	private void boardFilter() {
		// BOARD 
		Filter_SQL_MAP.put("SELECT_PART_CATE", "AND BOARD_CATEGORY_NUM = ?");
		Filter_SQL_MAP.put("SELECT_PART_PERIOD", "AND BOARD_WRITE_DAY >= NOW() - INTERVAL ? DAY");
		Filter_SQL_MAP.put("SELECT_PART_TITLE", "AND BOARD_TITLE LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("SELECT_PART_NICKNAME", "AND MEMBER_NICKNAME LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("SELECT_PART_CONTENT", "AND BOARD_CONTENT LIKE CONCAT('%', ?, '%')");
	}
}

