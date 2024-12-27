package com.bungeobbang.app.biz.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.filterSearch.StoreFilter;

@Repository
public class StoreDAO {
   @Autowired
   private JdbcTemplate jdbcTemplate;

   //가게 기본정보 추가
   //받은 데이터 : 가게 상호명, 기본&상세주소, 가게 전화번호
   //추가 데이터 : 가게 고유번호(PK), 가게 상호명, 기본&상세주소, 가게 전화번호, 가게폐점여부(N-자동등록)
   private final String INSERT = "INSERT INTO BB_STORE"
         + "(STORE_NAME, STORE_ADDRESS, STORE_ADDRESS_DETAIL, STORE_CONTACT, "
         + "STORE_CLOSED, STORE_SECRET) VALUES (?, ?, ?, ?, ?, ?)";

   private final String DELETE_STORE = "DELETE FROM BB_STORE WHERE STORE_NUM = ?";
   //특정가게 상세정보 조회
   //받은 데이터 : 가게 고유번호(PK)
   //조회 데이터 : 가게 고유번호(PK), 가게 상호명, 기본&상세주소, 가게 전화번호, 가게폐점여부, 비공개 여부
   private final String INFO_STORE_SELECTONE = """
            SELECT 
            STORE_NUM, 
            STORE_NAME,
            STORE_ADDRESS,
            STORE_ADDRESS_DETAIL,
            STORE_CONTACT,
            STORE_CLOSED, 
            STORE_SECRET, 
            STORE_MENU_NORMAL, 
            STORE_MENU_VEG, 
            STORE_MENU_POTATO, 
            STORE_MENU_MINI, 
            STORE_MENU_ICE, 
            STORE_MENU_CHEESE, 
            STORE_MENU_PASTRY, 
            STORE_MENU_OTHER, 
            STORE_PAYMENT_CASHMONEY, 
            STORE_PAYMENT_CARD, 
            STORE_PAYMENT_ACCOUNT, 
            STORE_DECLARED
         FROM 
            BB_VIEW_STORE_JOIN 
         WHERE 
            STORE_NUM = ? """;


   //가게 고유번호(PK) 최댓값 조회(+insert 과정에 활용)
   private final String STORE_NEW_SELECTONE = "SELECT MAX(STORE_NUM) AS MAX_S_NUM FROM BB_STORE";

   //가게 수 조회
   private final String STORE_CNT_SELECTONE = "SELECT COUNT(*) AS CNT FROM BB_VIEW_SEARCHSTOREDATA_JOIN ";

   //필터링 후 해당하는 가게 고유번호(PK) 모두 조회
   private final String SELECTALL_VIEW = """
         SELECT 
            STORE_NUM, 
            STORE_NAME, 
               STORE_ADDRESS,
               STORE_ADDRESS_DETAIL,
               STORE_CONTACT,
            STORE_CLOSED, 
            STORE_SECRET, 
            STORE_MENU_NORMAL, 
            STORE_MENU_VEG, 
            STORE_MENU_POTATO, 
            STORE_MENU_MINI, 
            STORE_MENU_ICE, 
            STORE_MENU_CHEESE, 
            STORE_MENU_PASTRY, 
            STORE_MENU_OTHER, 
            STORE_PAYMENT_CASHMONEY, 
            STORE_PAYMENT_CARD, 
            STORE_PAYMENT_ACCOUNT, 
            STORE_DECLARED
         FROM 
            BB_VIEW_STORE_JOIN """;

   //주소검색
   private final String SELECTALL_ADDRESS = """
         SELECT 
            STORE_NUM, 
            STORE_NAME, 
               STORE_ADDRESS,
               STORE_ADDRESS_DETAIL,
               STORE_CONTACT,
            STORE_CLOSED, 
            STORE_SECRET, 
            STORE_MENU_NORMAL, 
            STORE_MENU_VEG, 
            STORE_MENU_POTATO, 
            STORE_MENU_MINI, 
            STORE_MENU_ICE, 
            STORE_MENU_CHEESE, 
            STORE_MENU_PASTRY, 
            STORE_MENU_OTHER, 
            STORE_PAYMENT_CASHMONEY, 
            STORE_PAYMENT_CARD, 
            STORE_PAYMENT_ACCOUNT, 
            STORE_DECLARED
         FROM 
            BB_VIEW_STORE_JOIN 
         WHERE
             STORE_ADDRESS LIKE concat('%', ? ,'%')
            """;

   private final String SELECTALL_DECLARED_CNT="""
         SELECT 
            S.STORE_NUM, 
            S.STORE_NAME, 
              STORE_ADDRESS,
               STORE_ADDRESS_DETAIL,
               STORE_CONTACT,
            S.STORE_CLOSED, 
            S.STORE_SECRET, 
            S.STORE_MENU_NORMAL, 
            S.STORE_MENU_VEG, 
            S.STORE_MENU_POTATO, 
            S.STORE_MENU_MINI, 
            S.STORE_MENU_ICE, 
            S.STORE_MENU_CHEESE, 
            S.STORE_MENU_PASTRY, 
            S.STORE_MENU_OTHER, 
            S.STORE_PAYMENT_CASHMONEY, 
            S.STORE_PAYMENT_CARD, 
            S.STORE_PAYMENT_ACCOUNT, 
            S.STORE_DECLARED, 
            COALESCE(D.DECLARED_COUNT, 0) AS DECLARED_COUNT 
         FROM 
            BB_VIEW_STORE_JOIN S
         LEFT JOIN (
               SELECT 
               STORE_NUM, 
               COUNT(*) AS DECLARED_COUNT
            FROM 
               BB_DECLARE
            GROUP BY 
               STORE_NUM
            ) D 
            ON S.STORE_NUM = D.STORE_NUM
         WHERE DECLARED_COUNT >= 3
         """;

   private final String SELECTALL_VISIBLE_LIST = """
         SELECT bs.STORE_NUM, bs.STORE_NAME, bs.STORE_ADDRESS, bs.STORE_ADDRESS_DETAIL, bs.STORE_CONTACT, bs.STORE_CLOSED, bs.STORE_SECRET,
            bsm.STORE_MENU_NORMAL, bsm.STORE_MENU_VEG, bsm.STORE_MENU_MINI, bsm.STORE_MENU_POTATO, bsm.STORE_MENU_ICE, bsm.STORE_MENU_CHEESE, bsm.STORE_MENU_PASTRY, bsm.STORE_MENU_OTHER,
            bsp.STORE_PAYMENT_CASHMONEY, bsp.STORE_PAYMENT_CARD, bsp.STORE_PAYMENT_ACCOUNT 
         FROM bb_store bs
            LEFT JOIN bb_store_menu bsm ON bs.STORE_NUM = bsm.STORE_NUM
            LEFT JOIN bb_store_payment bsp ON bs.STORE_NUM = bsp.STORE_NUM
         ORDER BY bs.STORE_SECRET desc, bs.STORE_NUM desc;
         """;

   //항상 조건절 충족하도록 WHERE 1=1 변수 생성
   private final String SELECTALLNUMFILTER = " WHERE 1=1 ";

   private final String SELECTALL_ENDPART = " LIMIT ?,?";


   // UPDATE_필터 변수 모음 ------------------------------------------------------------------------------------------------------
   private final String UPDATE_SET = "UPDATE BB_STORE SET ";

   // 가게 번호 변수
   private final String WHERE_STORENUM = "WHERE STORE_NUM = ?";

   private final int CONTENT_SIZE = 4; //페이지 데이터 개수

   // StoreDAO insert --------------------------------------------------------------------------------------

   public boolean insert(StoreDTO storeDTO) { // 신규등록가게 추가
      System.out.println("log_StoreDAO_insert : start");
      System.out.println("log_StoreDAO_insert_controller input StoreDTO : " + storeDTO);
      // 비정상 프로그램 종료 방지 위한 try-catch 진행
      try {
         //SQL DB와 연결하여 INSERT 변수값 미리 컴파일, 실행 준비
         // INSERT 쿼리문 실행
         int rs = jdbcTemplate.update(INSERT,       // input값 storeDTO 이하 입력
               storeDTO.getStoreName(),          // 상호명 입력
               storeDTO.getStoreAddress(),       // 기본주소 입력
               storeDTO.getStoreAddressDetail(),    // 상세주소 입력
               storeDTO.getStoreContact(),         // 전화번호 입력
               storeDTO.getStoreClosed(),          //폐점 여부
               storeDTO.getStoreSecret()         // 공개 여부
         );
         System.out.println("log_StoreDAO_update_rs complete");

         if (rs <= 0) {//rs >= 1(success) / rs = 0 (fail)
            System.err.println("log_StoreDAO_insert_executeUpdate() fail : if(rs <= 0)");
            return false;
         }
      } catch (Exception e) {
         System.err.println("log_StoreDAO_insert_Exception fail : Exception e ");
         e.printStackTrace();
         return false;
      }
      System.out.println("log_StoreDAO_insert_true!");
      return true;
   }

   // StoreDAO update 가게 기본 정보 수정
   // ------------------------------------------------------------------------------
   public boolean update(StoreDTO storeDTO) {
      System.out.println("log_StoreDAO_update : start");
      System.out.println("log_StoreDAO_update_controller input StoreDTO : " + storeDTO);
      String query =""; //쿼리문 초기화
      Object[] args = null;
      // 비정상 프로그램 종료 방지 위한 try-catch 진행

      //if(storeDTO.getCondition().equals("UPDATE_STORE")) {

      System.out.println("log_StoreDAO_update_queryBuilder UPDATE_SET setting");
      //SET절 추가 및 쿼리문 형성
      HashMap<String, String>filters = storeDTO.getFilterList();
      StoreFilter filterUtil = new StoreFilter();
      List<Object> argsList = new ArrayList<>();
      query = UPDATE_SET;
      query = filterUtil.buildFilterQuery(query, filters).toString().replaceAll(",$", "");
      // 마지막 쉼표 삭제

      //쿼리문 형성 완료
      query = query+" "+WHERE_STORENUM;
      System.out.println("log : UPDATE query = "+query);
      //키워드 세팅
      argsList = filterUtil.setFilterKeywords(argsList,filters);
      argsList.add(storeDTO.getStoreNum());

      args = argsList.toArray(); //args 배열화

      //[6] rs 변수 선언 : UPDATE 쿼리문 실행
      try {
         int rs = jdbcTemplate.update(query, args);
         if (rs <= 0) {
            System.err.println("log_StoreDAO_update_executeUpdate() fail : if(rs <= 0)");
            return false;
         }
      }
      //[7] 예외처리 : 정상실행 되지 않았을 경우, false
      catch (Exception e) {
         System.err.println("log_StoreDAO_update_Exception fail : Exception e ");
         e.printStackTrace();
         return false;
      }
      System.out.println("log_StoreDAO_update_true!");
      return true; //반환값 true
//      }
//      else {
//         System.err.println("log: Store update condition fail");
//         return false;
//      }
   }

   public boolean delete(StoreDTO storeDTO) {
      System.out.println("log_StoreDAO_delete : start");
      System.out.println("log_StoreDAO_detle_controller input StoreDTO : " + storeDTO);

      // 비정상 프로그램 종료 방지 위한 try-catch 진행
      try {

         System.out.println("log_StoreDAO_delete_queryBuilder DELETE_STORE setting");

         //쿼리문 수행
         int rs = jdbcTemplate.update(DELETE_STORE,
               storeDTO.getStoreNum()
         );
         //[7] 예외처리 : 정상실행 되지 않았을 경우, false
         if (rs <= 0) {
            System.err.println("log_StoreDAO_update_executeUpdate() fail : if(rs <= 0)");
            return false;
         }
      } catch (Exception e) {
         System.err.println("log_StoreDAO_update_Exception fail : Exception e ");
         e.printStackTrace();
         return false;
      }
      System.out.println("log_StoreDAO_update_true!");
      return true;
   }

   //  StoreDAO selectAll ------------------------------------------------------------------------------------
   public List<StoreDTO> selectAll(StoreDTO storeDTO) { // 검색 가게 모두 조회
      System.out.println("log_StoreDAO_selectaAll : start");
      System.out.println("log_StoreDAO_selectAll controller input StoreDTO : " + storeDTO.toString());
      //datas 변수 선언 : 결과값 담을 datas
      List<StoreDTO> datas = new ArrayList<StoreDTO>();
      //필터 검색 객체 생성
      String query= ""; //쿼리문 초기화
      try {
         if(storeDTO.getCondition().equals("SELECTALL_VIEW_FILTER")){ //전체검색
            System.out.println("log : Store selectAll : SELECTALL_VIEW");
            HashMap<String, String> filters = storeDTO.getFilterList();
            StoreFilter filterUtil = new StoreFilter();
            List<Object> argsList = new ArrayList<>();
            //메서드를 통해 쿼리문을 완성한 후 toString을 통해 다시 String으로 변환
            query = SELECTALL_VIEW +" "+ SELECTALLNUMFILTER; // 필터 검색 시작 쿼리문

            query = filterUtil.buildFilterQuery(query,filters)+" "+SELECTALL_ENDPART;
            argsList = filterUtil.setFilterKeywords(argsList, filters); //필터 검색 검색어

            argsList.add(storeDTO.getStartNum() < 1? 0: storeDTO.getStartNum()-1);
            argsList.add(CONTENT_SIZE); // 페이지 네이션 시작, 끝 번호

            //args 배열화
            Object[] args = argsList.toArray();
            System.out.println("log Store selectAll args = "+Arrays.toString(args));
            datas = jdbcTemplate.query(query, args, new StoreRowMapper());
         }
         else if(storeDTO.getCondition().equals("SELECTALL_DECLARED_CNT")) {
            // 신고 개수 조회
            System.out.println("log : Store selectAll : SELECTALL_DECLARED_CNT");
            query = SELECTALL_DECLARED_CNT;
            datas = jdbcTemplate.query(query, new StoreRowMapper());
         }
         else if(storeDTO.getCondition().equals("SELECTALL_ADDRESS")) {
            //가게 주소 검색
            System.out.println("log : Store selectAll : SELECTALL_ADDRESS");
            query = SELECTALL_ADDRESS;
            Object[] args = {storeDTO.getStoreAddress()};
            datas = jdbcTemplate.query(query, args, new StoreRowMapper());
         }
         else if(storeDTO.getCondition().equals("STORE_TIP_OFF_LIST")) {
            //가게 비공개 조회
            System.out.println("log : Store selectAll : STORE_TIP_OFF_LIST");
            datas = jdbcTemplate.query(SELECTALL_VISIBLE_LIST, (rs, i)->{
               StoreDTO dto = new StoreDTO();
               dto.setStoreNum(rs.getInt("STORE_NUM"));
               dto.setStoreName(rs.getString("STORE_NAME"));
               dto.setStoreAddress(rs.getString("STORE_ADDRESS"));
               dto.setStoreAddressDetail(rs.getString("STORE_ADDRESS_DETAIL"));
               dto.setStoreContact(rs.getString("STORE_CONTACT"));
               dto.setStoreClosed(rs.getString("STORE_CLOSED"));
               dto.setStoreSecret(rs.getString("STORE_SECRET"));
               //storeMenu
               dto.setStoreMenuNormal(rs.getString("STORE_MENU_NORMAL"));
               dto.setStoreMenuVeg(rs.getString("STORE_MENU_VEG"));
               dto.setStoreMenuMini(rs.getString("STORE_MENU_MINI"));
               dto.setStoreMenuPotato(rs.getString("STORE_MENU_POTATO"));
               dto.setStoreMenuIce(rs.getString("STORE_MENU_ICE"));
               dto.setStoreMenuCheese(rs.getString("STORE_MENU_CHEESE"));
               dto.setStoreMenuPastry(rs.getString("STORE_MENU_PASTRY"));
               dto.setStoreMenuOther(rs.getString("STORE_MENU_OTHER"));
               // 결제 방식
               dto.setStorePaymentCard(rs.getString("STORE_PAYMENT_CARD"));
               dto.setStorePaymentCashMoney(rs.getString("STORE_PAYMENT_CASHMONEY"));
               dto.setStorePaymentAccount(rs.getString("STORE_PAYMENT_ACCOUNT"));
               return dto;
            });
         }
         else {
            //컨디션 오류
            System.err.println("log: Store selectAll condition fail");
            return null;
         }
      }
      catch(Exception e) {
         System.err.println("log : Store selectAll Exception fail");
         e.printStackTrace();
         return null;
      }

      System.out.println("log: Store selectAll return datas");
      return datas;
   }

   // StoreDAO selectOne ----------------------------------------------------------------------------------------------------------------------------
   public StoreDTO selectOne(StoreDTO storeDTO) {
      System.out.println("log_StoreDAO_selectOne : start");
      System.out.println("log_StoreDAO_selectOne controller input StoreDTO : " + storeDTO);
      String query = "";
      StoreDTO data = new StoreDTO();
      try {
         // 값이 가장 큰 가게고유번호(PK) 조회(+insert 과정에 활용)---------------------------------------------------
         if (storeDTO.getCondition().equals("STORE_NEW_SELECTONE")) {
            System.out.println("log_StoreDAO_selectOne condition : STORE_NEW_SELECTONE");

            query = STORE_NEW_SELECTONE;

            data = jdbcTemplate.queryForObject(query, new MaxRowMapper());
         }
         else if(storeDTO.getCondition().equals("STORE_CNT_SELECTONE")) {
            //필터링된 가게 수
            System.out.println("log : SelectOne STORE_CNT_SELECTONE");
            HashMap<String, String> filters = storeDTO.getFilterList();
            StoreFilter filterUtil = new StoreFilter();

            query = STORE_CNT_SELECTONE + " " + SELECTALLNUMFILTER;
            //메서드를 통해 쿼리문을 완성한 후 toString을 통해 다시 String으로 변환
            query = filterUtil.buildFilterQuery(query,filters).toString();
            List<Object> argsList = new ArrayList<>();
            argsList = filterUtil.setFilterKeywords(argsList, filters); //필터 검색 검색어
            //args 배열화
            Object[] args = argsList.toArray();
            System.out.println("log Store selectOne args ="+Arrays.toString(args));
            data = jdbcTemplate.queryForObject(query,args,new CntRowMapper());
         }
         // 특정가게 상세정보 조회---------------------------------------------------------------------------------
         else if (storeDTO.getCondition().equals("INFO_STORE_SELECTONE")) {
            System.out.println("log_StoreDAO_selectOne_condition : INFO_STORE_SELECTONE");

            query = INFO_STORE_SELECTONE;
            Object[] args = {storeDTO.getStoreNum()};

            data = jdbcTemplate.queryForObject(query, args, new StoreRowMapper());
            System.out.println("log_StoreDAO_selectOne_executeQuery() complete");
         }
         System.out.println("log_StoreDAO_selectOne_complet!");
      } catch (Exception e) {
         System.err.println("log_StoreDAO_selectOne_Exception fail : Exception e ");
         e.printStackTrace();
         return null;
      }
      System.out.println("log_StoreDAO_selectOne_SEARCH_STORE_SELECTOne return data!");
      return data; // 데이터 반환
   }
   class StoreRowMapper implements RowMapper<StoreDTO>{

      @Override
      public StoreDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         StoreDTO data = new StoreDTO();
         data.setStoreNum(rs.getInt("STORE_NUM"));                     // 가게 고유번호
         data.setStoreName(rs.getString("STORE_NAME"));                   // 가게 상호명
         data.setStoreAddress(rs.getString("STORE_ADDRESS"));       // 가게 기본주소
         data.setStoreAddressDetail(rs.getString("STORE_ADDRESS_DETAIL"));    // 가게 상세주소
         data.setStoreContact(rs.getString("STORE_CONTACT"));            // 가게 전화번호
         data.setStoreClosed(rs.getString("STORE_CLOSED"));                // 가게 폐점여부
         data.setStoreSecret(rs.getString("STORE_SECRET"));                // 가게 공개 여부
         data.setStoreDeclared(rs.getString("STORE_DECLARED"));             // 가게 신고 여부
         //storeMenu
         data.setStoreMenuNormal(rs.getString("STORE_MENU_NORMAL"));
         data.setStoreMenuVeg(rs.getString("STORE_MENU_VEG"));
         data.setStoreMenuMini(rs.getString("STORE_MENU_MINI"));
         data.setStoreMenuPotato(rs.getString("STORE_MENU_POTATO"));
         data.setStoreMenuIce(rs.getString("STORE_MENU_ICE"));
         data.setStoreMenuCheese(rs.getString("STORE_MENU_CHEESE"));
         data.setStoreMenuPastry(rs.getString("STORE_MENU_PASTRY"));
         data.setStoreMenuOther(rs.getString("STORE_MENU_OTHER"));
         // 결제 방식
         data.setStorePaymentCard(rs.getString("STORE_PAYMENT_CARD"));
         data.setStorePaymentCashMoney(rs.getString("STORE_PAYMENT_CASHMONEY"));
         data.setStorePaymentAccount(rs.getString("STORE_PAYMENT_ACCOUNT"));

         System.out.println("log_StoreDAO_selectAll_data : " + data);
         return data;
      }

   }
   class MaxRowMapper implements RowMapper<StoreDTO>{

      @Override
      public StoreDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         StoreDTO data = new StoreDTO(); // 데이터 요소 담기 위한 객체 생성
         data.setMaxPK(rs.getInt("MAX_S_NUM"));
         System.out.println("log_StoreDAO_selectOne_data finish : " + data);
         return data;
      }

   }
   class CntRowMapper implements RowMapper<StoreDTO>{

      @Override
      public StoreDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         StoreDTO data = new StoreDTO(); // 데이터 요소 담기 위한 객체 생성
         data.setCnt(rs.getInt("CNT"));
         System.out.println("log_StoreDAO_selectOne_data finish : " + data);
         return data;
      }

   }
}