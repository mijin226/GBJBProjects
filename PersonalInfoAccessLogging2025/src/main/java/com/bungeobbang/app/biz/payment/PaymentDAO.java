package com.bungeobbang.app.biz.payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.filterSearch.PaymentFilter;

@Repository
public class PaymentDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//insert 쿼리
	private final String INSERT_PAYMENT = "INSERT INTO BB_PAYMENT(MEMBER_NUM, PAYMENT_AMOUNT,PAYMENT_NAME,IMP_UUID) "
			+ "VALUES(?,?,?,?)";
	//update 쿼리
	private final String UPDATE_ADMINCHECK = "UPDATE BB_PAYMENT SET ADMIN_CHECKED = 'Y' WHERE PAYMENT_NUM = ?";

	//selectAll 쿼리
	private final String SELECTALL = """
         SELECT
            P.PAYMENT_NUM,
            P.MEMBER_NUM,
            P.PAYMENT_AMOUNT,
            P.ADMIN_CHECKED,
            P.PAYMENT_AT,
            P.PAYMENT_NAME,
            M.MEMBER_EMAIL,
            P.IMP_UUID
         FROM
            BB_PAYMENT P
         JOIN 
            BB_MEMBER M
         ON
            P.MEMBER_NUM = M.MEMBER_NUM
         WHERE
            P.MEMBER_NUM = ?
         """;

	private final String NUMFILTER = "WHERE 1=1";

	private final String SELECTALL_ENDPART = "ORDER BY PAYMENT_NUM LIMIT ?,?";

	//selectOne 쿼리 미사용

	public boolean insert(PaymentDTO paymentDTO) {
		System.out.println("log: Payment insert start");
		String query = ""; //쿼리문 초기화
		Object[] args = null; // args 배열 초기화
		int rs = 0;
		if(paymentDTO.getCondition().equals("INSERT_PAYMENT")) {
			// 결제 내역 생성
			System.out.println("log : Payment insert : INSERT_PAYMENT");
			query= INSERT_PAYMENT;
			args=new Object[] {
					paymentDTO.getMemberNum(),		//회원 번호
					paymentDTO.getPaymentAmount(),	//결제 금액	
					paymentDTO.getPaymentName(), 	// 결제 상품명
				    paymentDTO.getImpUUid() 		 // UUID
			};

			System.out.println("log: parameter paymentDTO ["+paymentDTO+"]");
		}
		else {
			System.err.println("log : Payment insert condition fail");
		}
		//쿼리문 실행
		try{
			rs = jdbcTemplate.update(query, args);
		}catch(Exception e ) {
			System.err.println("log : Payment insert Exception fail");
			return false;
		}
		//컨디션 오류

		if(rs<=0) {
			// 쿼리는 정상적으로 실행됐으나 실패
			System.err.println("log : Payment insert execute fail");
			return false;
		}

		System.out.println("log : Payment insert true");
		return true;
	}


	public boolean update(PaymentDTO paymentDTO) {
		System.out.println("log: Payment update start");
		Object[] args = null;
		String query = "";
		int rs = 0;
		try{
			if(paymentDTO.getCondition().equals("ADMIN_CHECK")) {
				// 관리자 확인 수정
				System.out.println("log : Payment update : ADMIN_CHECK");
				query = UPDATE_ADMINCHECK;
				args=new Object[] {
						paymentDTO.getPaymentNum() //회원번호
				};
				System.out.println("log: parameter getPaymentNum : "+paymentDTO.getPaymentNum());
			}
			else {
				//컨디션 오류
				System.err.println("log : Payment update condition fail");
			}
			System.out.println("log : Payment update pstmt excute : ");
			rs = jdbcTemplate.update(query, args);

			if(rs<=0) {
				// 쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log : Payment update execute fail");
				return false;
			}
		}catch(Exception e ) {
			System.err.println("log : Payment update Exception fail");
			e.printStackTrace();
			return false;
		}
		System.out.println("log : Payment update true");
		return true;
	}
	private boolean delete(PaymentDTO paymentDTO) {
		return false;
	}

	public List<PaymentDTO> selectAll(PaymentDTO paymentDTO){
		System.out.println("log: Payment selectAll start");
		List<PaymentDTO> datas = new ArrayList<>();
		Object[] args = null;
		String query = ""; //쿼리문 초기화

		if(paymentDTO.getCondition().equals("SELECTALL_PAYMENT")) {
			//지불 내역 전체 검색
			System.out.println("log : Payment selectAll : SELECTALL_PAYMENT");
			query = SELECTALL;
			args = new Object[]{paymentDTO.getMemberNum()};
		}
		else {
			//컨디션값 오류
			System.err.println("log : Payment selectAll condition fail");
		}
		// 쿼리문 실행
		System.out.println("log : Payment query = "+query);
		try {
			datas = jdbcTemplate.query(query, args, new PaymentRowMapper());
		}catch (Exception e) {
			System.err.println("log: Payment selectAll Exception fail");
			e.printStackTrace();
			return null;
		}
		System.out.println("log: Payment selectAll return datas");
		return datas;
	}

	private PaymentDTO selectOne(PaymentDTO paymentDTO) {
		System.out.println("log: Payment selectOne start");
		Object[] args = null;
		String query = "";
		PaymentDTO data = new PaymentDTO();
		if(paymentDTO.getCondition().equals("SELECTONE_PAYMENT")) {
			System.out.println("log : Payment selectOne : SELECTONE_PAYMENT");
//			query = SELECTONE;
			args = new Object[] {paymentDTO.getPaymentNum()};
		}
		else {
			//컨디션값 오류
			System.err.println("log: Payment selectOne condition fail");
		}
		try {
			data = jdbcTemplate.queryForObject(query, args, new PaymentRowMapper());
			System.out.println("end");
		} 
		catch (Exception e) {
			System.err.println("log: Payment selectOne Exception fail");
			e.printStackTrace();
			return null;
		} 		
		System.out.println("log: Payment selectOne return datas");
		return data;
	}
	class PaymentRowMapper implements RowMapper<PaymentDTO>{

		@Override
		public PaymentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentDTO data = new PaymentDTO();
			data.setPaymentNum(rs.getInt("PAYMENT_NUM")); //지불내역 번호
			data.setMemberNum(rs.getInt("MEMBER_NUM")); //회원번호
			data.setPaymentAmount(rs.getInt("PAYMENT_AMOUNT")); // 결제 금액
			data.setAdminChecked(rs.getString("ADMIN_CHECKED")); // 관리자 확인 여부
			data.setPaymentAt(rs.getString("PAYMENT_AT"));
			data.setPaymentName(rs.getString("PAYMENT_NAME"));
			data.setMemberEmail(rs.getString("MEMBER_EMAIL"));
			data.setImpUUid(rs.getString("IMP_UUID"));
			System.out.println("log : result ["+data.getPaymentNum()+"]");
			return data;
		}

	}
}	
