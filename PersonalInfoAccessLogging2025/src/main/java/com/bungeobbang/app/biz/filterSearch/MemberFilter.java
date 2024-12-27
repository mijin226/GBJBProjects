package com.bungeobbang.app.biz.filterSearch;

public class MemberFilter extends FilterSearchUtil{
	public MemberFilter() {
		super();
		memberFilter();
	}
	private void memberFilter() {
		// MEMBER  
		Filter_SQL_MAP.put("SELECT_PART_EMAIL", "AND MEMBER_EMAIL LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("SELECT_PART_NICKNAME", "AND MEMBER_NICKNAME LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("SELECT_PART_NAME", "AND MEMBER_NAME LIKE CONCAT('%', ?, '%')");
		Filter_SQL_MAP.put("SELECT_PART_ROLE", "AND MEMBER_ROLE = ?");
		Filter_SQL_MAP.put("SELECT_PART_PHONE", "AND MEMBER_PHONE LIKE CONCAT('%', ?, '%')");
		
	}
}

