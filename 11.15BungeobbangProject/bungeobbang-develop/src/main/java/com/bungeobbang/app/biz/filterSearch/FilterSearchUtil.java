package com.bungeobbang.app.biz.filterSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class FilterSearchUtil {
	//키값과 SQL 매핑
	// static으로 선언된 필드는 객체를 생성하지 않고도 클래스 이름으로 접근 가능
	protected final Map<String, String> Filter_SQL_MAP;
	public FilterSearchUtil() {
		//기본 생성자
		Filter_SQL_MAP = new HashMap<String, String>();
	}

	public StringBuilder buildFilterQuery(String startQuery, HashMap<String, String> filters) {
		// startQuery = 시작 쿼리문
		// filters = <키값(C에서 보내는 값), value>
		System.out.println("log : buildFilterQuery start");
		StringBuilder query = new StringBuilder(startQuery);

		if(filters != null && !filters.isEmpty()) {
			for(String key : filters.keySet()) {
				// 키에 따른 value(SQL 조건) 추가
				String sql = Filter_SQL_MAP.get(key);
				if(sql != null) {
					query.append(" ").append(sql);
				}
				else {
					System.out.println("log : check FilterList key");
				}
			}
		}
		System.out.println("log: buildFilterQuery end");
		return query;
	}

	// PreparedStatement 파라미터 채우기
	public List<Object> setFilterKeywords(List<Object> argsList, HashMap<String, String> filters){
		//Store 관련 필터 키워드 추가 설정
		System.out.println("log : setFilterKeywords start");
		if(filters !=null && !filters.isEmpty()) {
			try {
				// filters Map만큼 반복
				for (Entry<String, String> keyword : filters.entrySet()) {
					// 해당 Map의 값을 리스트에 추가
					argsList.add(keyword.getValue());
				}
			}
			catch (Exception e) {
				System.err.println("log: setFilterKeywords "+e.getMessage());
				return null;
			}
		}
		System.out.println("log: setFilterKeywords end");
		return argsList;
	}
}



