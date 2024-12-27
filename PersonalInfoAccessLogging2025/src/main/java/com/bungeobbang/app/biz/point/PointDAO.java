package com.bungeobbang.app.biz.point;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bungeobbang.app.biz.common.JDBCUtil;
import org.springframework.stereotype.Repository;

@Repository
public class PointDAO {

	//insert 쿼리문
	private final String INSERT_POINT = "INSERT INTO BB_POINT(MEMBER_NUM, POINT_PLUS, POINT_MINUS, POINT_CONTENT) "
			+ "VALUES(?,?,?,?)";

	//selectAll 쿼리문
	private final String SELECTALL_POINT = "SELECT POINT_NUM, MEMBER_NUM, POINT_PLUS, POINT_MINUS, POINT_CONTENT "
			+ "FROM BB_POINT WHERE MEMBER_NUM = ?";

	//selectOne 쿼리문
	private final String SELECTONE_POINT = "SELECT POINT_NUM, MEMBER_NUM, POINT_PLUS, POINT_MINUS, POINT_CONTENT "
			+ "FROM BB_POINT WHERE POINT_NUM=? ";

	private final String SELECTONE_MEMBER_POINT = "SELECT MEMBER_NUM, " 
			+ "SUM(IFNULL(POINT_PLUS,0) - IFNULL(POINT_MINUS,0)) AS TOTAL_POINT "
			+ "FROM BB_POINT "
			+ "WHERE MEMBER_NUM = ? "
			+ "GROUP BY MEMBER_NUM ";

	private boolean insert(PointDTO pointDTO) {

		return false;
	}

	private boolean update(PointDTO pointDTO) {

		return false;
	}

	private boolean delete(PointDTO pointDTO) {
		return false;
	}

	public List<PointDTO> selectAll(PointDTO pointDTO){
		System.out.println("log: Point selectAll start");
		List<PointDTO> datas = new ArrayList<>();
		Connection conn = JDBCUtil.connect();
		PreparedStatement pstmt = null;
		int placeholderNum = 1;
		try {
			//해당 회원의 모든 포인트 내역 보기
			System.out.println("log : Point selectAll : SELECTALL_POINT");
			pstmt= conn.prepareStatement(SELECTALL_POINT);
			pstmt.setInt(placeholderNum++, pointDTO.getMemberNum());
			System.out.println("log : Point selectAll query = "+pstmt.toString());
			//쿼리문 수행
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) { 
				PointDTO data = new PointDTO();
				data.setPointNum(rs.getInt("POINT_NUM"));
				data.setMemberNum(rs.getInt("MEMBER_NUM"));
				data.setPointPlus(rs.getInt("POINT_PLUS"));
				data.setPointMinus(rs.getInt("POINT_MINUS"));
				data.setPointContent(rs.getString("POINT_CONTENT"));
				datas.add(data);
			}
			rs.close();
			System.out.println("end");
		}catch (SQLException e) {
			System.err.println("log: Point selectAll SQLException fail");
			datas.clear();//잔여데이터 삭제
		} catch (Exception e) {
			System.err.println("log: Point selectAll Exception fail");
			datas.clear();//잔여데이터 삭제
		} finally {
			//연결해제
			if(!JDBCUtil.disconnect(conn, pstmt)) {
				//연결해제 실패
				System.err.println("log: Point selectAll disconnect fail");
				datas.clear();//잔여데이터 삭제
			}
			System.out.println("log: Point selectAll end");
		}
		System.out.println("log: Point selectAll return datas");
		return datas;
	}
	public PointDTO selectOne(PointDTO pointDTO) { 
		System.out.println("log: Point selectOne start");
		Connection conn = JDBCUtil.connect();
		PreparedStatement pstmt = null;
		PointDTO data = new PointDTO();
		int placeholderNum=1;
		try {
			//상세보기 (미사용)
			if(pointDTO.getCondition().equals("SELECTONE_POINT")) {
				System.out.println("log : Point selectOne : SELECTONE_POINT");
				pstmt=conn.prepareStatement(SELECTONE_POINT);
				pstmt.setInt(placeholderNum++, pointDTO.getPointNum());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					data.setPointNum(rs.getInt("POINT_NUM"));
					data.setMemberNum(rs.getInt("MEMBER_NUM"));
					data.setPointPlus(rs.getInt("POINT_PLUS"));
					data.setPointMinus(rs.getInt("POINT_MINUS"));
					data.setPointContent(rs.getString("POINT_CONTENT"));
					System.out.println("log : result exists");
				}
			}
			//회원 잔여 포인트
			else if(pointDTO.getCondition().equals("SELECTONE_MEMBER_POINT") ) {
				System.out.println("log : Point selectOne : SELECTONE_MEMBER_POINT");
				pstmt = conn.prepareStatement(SELECTONE_MEMBER_POINT);
				pstmt.setInt(placeholderNum++, pointDTO.getMemberNum());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					data.setMemberNum(rs.getInt("MEMBER_NUM"));
					data.setTotalMemberPoint(rs.getInt("TOTAL_POINT"));
					System.out.println("log : result exists");
				}
			}
			else {
				//컨디션값 오류
				System.err.println("log: Point selectOne condition fail");
			}
			System.out.println("end");
		} catch (SQLException e) {
			System.err.println("log: Point selectOne SQLException fail");
			return null;
		} catch (Exception e) {
			System.err.println("log: Point selectOne Exception fail");
			return null;
		} finally {
			//연결해제
			if(!JDBCUtil.disconnect(conn, pstmt)) {
				//연결해제 실패
				System.err.println("log: Point selectOne disconnect fail");
				return null;
			}
			System.out.println("log: Point selectOne end");
		}
		System.out.println("log: Point selectOne return datas");
		return data;
	}
}