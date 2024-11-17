package com.bungeobbang.app.biz.storeMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.common.JDBCUtil;

@Repository
public class StoreMenuDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//가게 메뉴정보 추가
	//받는 데이터 : 가게 고유번호(FK), 팥/슈프림, 야채/김치/만두, 미니, 고구마, 아이스크림/초코, 치즈, 페스츄리, 기타 판매 여부(Y/N)
	//추가 데이터 : 메뉴 고유번호(PK), 가게 고유번호(FK), 팥/슈프림, 야채/김치/만두, 미니, 고구마, 아이스크림/초코, 치즈, 페스츄리, 기타 판매 여부(Y/N)
	final String INSERT = "INSERT INTO BB_STORE_MENU (STORE_NUM, "
			+ "STORE_MENU_NORMAL, STORE_MENU_VEG, STORE_MENU_MINI, STORE_MENU_POTATO, "
			+ "STORE_MENU_ICE, STORE_MENU_CHEESE, STORE_MENU_PASTRY, STORE_MENU_OTHER) "
			+ "VALUES(?,?,?,?,?,?,?,?,?)";

	//가게 메뉴 정보 수정
	//받는 데이터 : 가게 고유번호(FK), 팥/슈프림, 야채/김치/만두, 미니, 고구마, 아이스크림/초코, 치즈, 페스츄리, 기타 판매 여부(Y/N)
	final String UPDATE = "UPDATE BB_STORE_MENU " +
			"SET STORE_MENU_NORMAL = ?, STORE_MENU_VEG = ?, " +
			"STORE_MENU_MINI = ?, STORE_MENU_POTATO = ?, " +
			"STORE_MENU_ICE = ?, STORE_MENU_CHEESE = ?, " +
			"STORE_MENU_PASTRY = ?, STORE_MENU_OTHER = ? " +
			"WHERE STORE_NUM = ?";

	//가게 별 검색 개수
	final String SELECTONE_CNT = "SELECT SUM(STORE_MENU_NORMAL = 'Y') AS STORE_MENU_NORMAL_CNT, " +
			"    SUM(STORE_MENU_VEG = 'Y') AS STORE_MENU_VEG_CNT, " +
			"    SUM(STORE_MENU_MINI = 'Y') AS STORE_MENU_MINI_CNT, " +
			"    SUM(STORE_MENU_POTATO = 'Y') AS STORE_MENU_POTATO_CNT, " +
			"    SUM(STORE_MENU_ICE = 'Y') AS STORE_MENU_ICE_CNT, " +
			"    SUM(STORE_MENU_CHEESE = 'Y') AS STORE_MENU_CHEESE_CNT, " +
			"    SUM(STORE_MENU_PASTRY = 'Y') AS STORE_MENU_PASTRY_CNT, " +
			"    SUM(STORE_MENU_OTHER = 'Y') AS STORE_MENU_OTHER_CNT FROM BB_STORE_MENU";


	//   StoreMenuDAO insert   ---------------------------------------------- ----------------------------------------   

	public boolean insert(StoreMenuDTO storeMenuDTO) { // 신규등록가게 메뉴추가
		System.out.println("log_StoreMenuDAO_insert : start");

		// 비정상 프로그램 종료 방지 위한 try-catch 진행
		try { 
			String query =INSERT;

			Object[] args = {
					storeMenuDTO.getStoreNum()             // 가게 고유번호(FK) 입력
					, storeMenuDTO.getStoreMenuNormal()       // 팥/슈프림 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuVeg()    // 야채/김치/만두 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuMini()       // 미니 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuPotato()       // 고구마 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuIce()    // 아이스크림/초코 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuCheese()      // 치즈 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuPastry()       // 페스츄리 붕어빵 판매 여부(Y/N) 입력
					, storeMenuDTO.getStoreMenuOther()
			};       // 기타 붕어빵 해당 여부(Y/N) 입력


			// rs 변수 선언 : INSERT 쿼리문 실행
			int rs = jdbcTemplate.update(query,args);
			System.out.println("log_StoreMenuDAO_insert_excuteUpdate() complete");

			//[6] 예외처리 : 정상실행 되지 않았을 경우, false
			if (rs <= 0) { //rs >= 1(success) / rs = 0 (fail)
				System.err.println("log_StoreMenuDAO_insert_executeUpdate() fail : if(rs <= 0)");
				return false; 
			}
		} catch (Exception e) {
			System.err.println("log_StoreMenuDAO_insert_Exception fail : Exception e ");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log : StoreMenu insert success");
		return true;
	}

	//   StoreMenuDAO Update ------------------------------------------------------------------------------------   
	//가게 메뉴 판매 정보 수정
	public boolean update(StoreMenuDTO storeMenuDTO) {
		System.out.println("log_StoreMenuDAO_update : start");
		String query = "";

		// 비정상 프로그램 종료 방지 위한 try-catch 진행
		query =UPDATE; // input값 storeDTO 이하 입력
		System.out.println("log_StoreMenuDAO_update_pstmt conn");

		//[4] 인자값으로 받은 데이터 쿼리문에 삽입
		Object[] args = {
				storeMenuDTO.getStoreMenuNormal()      // 팥/슈프림 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuVeg()   // 야채/김치/만두 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuMini()     // 미니 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuPotato()      // 고구마 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuIce()   // 아이스크림/초코 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuCheese()      // 치즈 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuPastry()      // 페스츄리 붕어빵 판매 여부(Y/N) 입력
				,storeMenuDTO.getStoreMenuOther()      // 기타 붕어빵 해당 여부(Y/N) 입력
				,storeMenuDTO.getStoreNum()         // 가게 고유번호 입력
		};

		try {
			//UPDATE 쿼리문 실행
			int rs = jdbcTemplate.update(query, args);

			if (rs <= 0) { //rs >= 1(success) / rs = 0 (fail)
				System.err.println("log_StoreMenuDAO_update_executeUpdate() fail : if(rs <= 0)");
				return false;
			}
		} catch (Exception e) {
			System.err.println("log_StoreMenuDAO_update_Exception fail : Exception e ");
			e.printStackTrace();
			return false; 
		}
		System.out.println("log_StoreMenuDAO_update_true!");
		return true;
	}

	private boolean delete(StoreMenuDTO storeMenuDTO) { // 신규등록가게 추가
		return false;
	}
	// StoreMenuDAO selectAll ------------------------------------------------------------------------------------   
	private ArrayList<StoreMenuDTO> selectAll(StoreMenuDTO storeMenuDTO) { // 메뉴 카테고리별 총 개수 조회
		return null; // 데이터 반환
	}
	//selectOne 특정가게 정보 조회---------------------------------------------------------------------------
	public StoreMenuDTO selectOne(StoreMenuDTO storeMenuDTO) {
		System.out.println("log_StoreMenuDAO_selectOne : start");
		System.out.println("log_StoreMenuDAO_selectOne controller input StoreMenuDTO : " + storeMenuDTO.toString());

		//[1] DB 연결 객체를 conn. 변수로 선언: JDBC 연결 관리하는 JDBCUtil 클래스에서 DB연결 설정 메서드 실행.
		Connection conn = JDBCUtil.connect();
		System.out.println("log_StoreMenuDAO__selectOne_conn setting complete");

		//[2] SQL 쿼리 미리 컴파일하는 객체 PreparedStatement를 참조하는 pstmt 변수 선언 및 초기화
		PreparedStatement pstmt = null;
		System.out.println("log_StoreMenuDAO__selectOne_psmt null setting complete");

		//[3] rs 변수 선언 : selectOne 쿼리문 실행
		ResultSet rs = null;
		System.out.println("log_StoreMenuDAO__selectOne_rs null setting complete");

		//[4] data 변수 선언 : 결과값 담을 data
		StoreMenuDTO data = null;
		System.out.println("log_StoreMenuDAO__selectOne_data null setting complete");

		try {
			pstmt = conn.prepareStatement(SELECTONE_CNT);
			//[7] rs 변수 선언 : SELECTONE 쿼리문 실행
			rs = pstmt.executeQuery();
			System.out.println("log_StoreMenuDAO_selectOne_executeQuery() complete");
			//[8] 특정 가게메뉴 카테고리별 붕어빵 판매 여부 불러오기
			if(rs.next()) {
				data = new StoreMenuDTO();
				data.setStoreMenuNormalCnt(rs.getInt("STORE_MENU_NORMAL_CNT"));    // 팥/슈프림 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuVegCnt(rs.getInt("STORE_MENU_VEG_CNT"));   // 야채/김치/만두 판매 여부(Y) 개수
				data.setStoreMenuMiniCnt(rs.getInt("STORE_MENU_MINI_CNT"));      // 미니 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuPotatoCnt(rs.getInt("STORE_MENU_POTATO_CNT"));   // 고구마 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuIceCnt(rs.getInt("STORE_MENU_ICE_CNT"));   // 아이스크림/초코 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuCheeseCnt(rs.getInt("STORE_MENU_CHEESE_CNT"));   // 치즈 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuPastryCnt(rs.getInt("STORE_MENU_PASTRY_CNT")); // 페스츄리 붕어빵 판매 여부(Y) 개수
				data.setStoreMenuOtherCnt(rs.getInt("STORE_MENU_OTHER_CNT"));    // 기타 붕어빵 해당 여부(Y) 개수
				System.out.println("log_StoreMenuDAO_selectOne_data : " + data);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("log_StoreMenuDAO_selectOne_Exception fail : Exception e ");
			//[9] JDBC 연결 해제 진행
		}finally {
			if(!JDBCUtil.disconnect(conn, pstmt)) {
				System.err.println("log_StoreMenuDAO_selectOne_disconnect fail");// 연결해제 실패
			}// JDBC 연결 해제 되었다면
			System.out.println("log_StoreMenuDAO_selectOne_complet"); // 연결해제 성공
		}
		System.out.println("log_StoreMenuDAO_selectOne_return data");
		return data; // 데이터 반환
	}
}   
