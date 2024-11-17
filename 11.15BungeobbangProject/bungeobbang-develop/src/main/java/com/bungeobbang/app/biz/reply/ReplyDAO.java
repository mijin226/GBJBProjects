package com.bungeobbang.app.biz.reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.common.JDBCUtil;

@Repository
public class ReplyDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String INSERT = "INSERT INTO BB_REPLY(REPLY_CONTENT, MEMBER_NUM, BOARD_NUM) VALUES(?, ?, ?)";
	private final String UPDATE = "UPDATE BB_REPLY SET REPLY_CONTENT = ? WHERE REPLY_NUM = ?";
	private final String DELETE = "DELETE FROM BB_REPLY WHERE REPLY_NUM = ?";

	private final String SELECTALL_BOARD = 
			"SELECT RN, REPLY_NUM, REPLY_CONTENT, MEMBER_NUM, MEMBER_NICKNAME, MEMBER_PROFILE_WAY, REPLY_WRITE_DAY " +
			"FROM ( " +
			"    SELECT ROW_NUMBER() OVER (ORDER BY br.REPLY_WRITE_DAY DESC) AS RN, " +
			"           br.REPLY_NUM, br.REPLY_CONTENT, br.MEMBER_NUM, " +
			"           bm.MEMBER_NICKNAME, bm.MEMBER_PROFILE_WAY, br.REPLY_WRITE_DAY " +
			"    FROM BB_REPLY br " +
			"    JOIN BB_MEMBER bm ON br.MEMBER_NUM = bm.MEMBER_NUM " +
			"    WHERE br.BOARD_NUM = ? " +
			") AS REPLY_ALL_TABLE " +
			"ORDER BY RN;";

	//	private final String SELECTALL_MYPAGE = "SELECT RN, REPLY_NUM, REPLY_CONTENT, MEMBER_NICKNAME, MEMBER_PROFILE_WAY, BOARD_NUM, REPLY_WRITE_DAY FROM (SELECT ROW_NUMBER() OVER(ORDER BY REPLY_WRITE_DAY DESC) AS RN, "
	//			+ " br.REPLY_NUM, br.REPLY_CONTENT, br.BOARD_NUM, bm.MEMBER_NICKNAME, bm.MEMBER_PROFILE_WAY ,br.REPLY_WRITE_DAY FROM BB_REPLY br  JOIN BB_MEMBER bm ON br.MEMBER_NUM = bm.MEMBER_NUM "
	//			+ "	WHERE br.MEMBER_NUM = ? ORDER BY br.REPLY_WRITE_DAY DESC) AS MY_REPLY_TABLE WHERE RN LIMIT ?, ?";

//	private final String SELECTONE = "SELECT br.REPLY_NUM, br.REPLY_CONTENT, br.MEMBER_NUM, bm.MEMBER_NICKNAME, bm.MEMBER_PROFILE_WAY ,br.REPLY_WRITE_DAY FROM BB_REPLY br JOIN BB_MEMBER bm ON br.MEMBER_NUM = bm.MEMBER_NUM "
//			+ " WHERE br.REPLY_NUM = ?";
	
	private final String SELECTONE_BOARD = "SELECT COUNT(*) AS CNT FROM BB_REPLY WHERE BOARD_NUM = ?";
	private final String SELECTONE_MYPAGE = "SELECT COUNT(*) AS CNT FROM BB_REPLY WHERE MEMBER_NUM = ?";

	public boolean insert(ReplyDTO replyDTO) {
		System.out.println("log: Reply insert start");
		String query = ""; //쿼리문 초기화
		int rs = 0; // 결과 초기화

		query = INSERT;
		Object[] args = {
				replyDTO.getReplyContent(), //댓글내용
				replyDTO.getMemberNum(), 		//멤버 번호
				replyDTO.getBoardNum()
		}; 		//게시글 번호
		//넘어온 값 확인 로그
		System.out.println("log: parameter getReplyContent : "+replyDTO.getReplyContent());
		System.out.println("log: parameter getMemberNum : "+replyDTO.getMemberNum());
		System.out.println("log: parameter getBoardNum : "+replyDTO.getBoardNum());
		try {
			rs = jdbcTemplate.update(query, args);
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Reply insert execute fail");
				return false;
			}
		} catch (Exception e) {
			System.err.println("log: Reply insert Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Reply insert true");
		return true;
	}

	public boolean update(ReplyDTO replyDTO) {
		System.out.println("log: Reply update start");
		String query = ""; //쿼리문 초기화
		int rs = 0; // 결과 초기화
		try {
			query =UPDATE;
			Object[] args = {
					replyDTO.getReplyContent(), //댓글 내용
					replyDTO.getReplyNum() 		//댓글 번호
			};
			//넘어온 값 확인 로그
			System.out.println("log: parameter getReplyContent : "+replyDTO.getReplyContent());
			System.out.println("log: parameter getReplyNum : "+replyDTO.getReplyNum());
			rs = jdbcTemplate.update(query, args);
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Reply update execute fail");
				return false;
			}
		}catch (Exception e) {
			System.err.println("log: Reply update Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Reply update true");
		return true;
	}

	public boolean delete(ReplyDTO replyDTO) {
		System.out.println("log: Reply delete start");
		String query = "";
		int rs = 0; //쿼리문과 결과 초기화
		try {
			query = DELETE;
			Object[] args = {
					replyDTO.getReplyNum() //댓글 번호
			}; 
			//넘어온 값 확인 로그
			System.out.println("log: parameter getReplyNum : "+replyDTO.getReplyNum());
			rs = jdbcTemplate.update(query, args);
			if(rs<= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Reply delete execute fail");
				return false;
			}
		}catch (Exception e) {
			System.err.println("log: Reply delete Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Reply delete true");
		return true;
	}

	public List<ReplyDTO> selectAll(ReplyDTO replyDTO) {
		System.out.println("log: Reply selectAll start");
		List<ReplyDTO> datas = new ArrayList<>();
		String query = "";
		Object[] args = null; //args 배열 초기화
		//게시물 댓글 조회
		System.out.println("log: Reply selectAll : ALL_REPLIES");
		query = SELECTALL_BOARD;
		args = new Object[] {						
				replyDTO.getBoardNum() 	//게시물 pk(Fk)
		};
		//넘어온 값 확인 로그
		System.out.println("log: parameter getBoardNum : "+replyDTO.getBoardNum());
		System.out.println("log: parameter getStartNum : "+replyDTO.getStartNum());
		System.out.println("log: parameter getEndNum : "+replyDTO.getEndNum());

		try {
			//쿼리문 수행
			datas = jdbcTemplate.query(query, args, new ReplyRowMapper());
		} catch (Exception e) {
			System.err.println("log: Reply selectAll Exception fail");
			datas.clear();//잔여데이터 삭제
		} 
		System.out.println("log: Reply selectAll return datas");
		return datas;
	}

	public ReplyDTO selectOne(ReplyDTO replyDTO) {
		System.out.println("log: Reply selectOne start");
		String query = "";
		Object[] args = null; //쿼리문과 args 초기화
		try {
			if(replyDTO.getCondition().equals("CNT_BOARD_RP")) {
				//게시물 댓글 조회
				System.out.println("log: Reply selectOne : CNT_BOARD_RP");
				query = SELECTONE_BOARD;
				args = new Object[] {replyDTO.getBoardNum()}; 	//게시물 pk(Fk)
				//넘어온 값 확인 로그
				System.out.println("log: parameter getBoardNum : "+replyDTO.getBoardNum());
				return jdbcTemplate.queryForObject(query,  args, new CntRowMapper());
			}
			else if(replyDTO.getCondition().equals("CNT_MY_RP")) {
				//내가 쓴 댓글 모아보기 (마이페이지)
				System.out.println("log: Reply selectOne : CNT_MY_RP");
				query = SELECTONE_MYPAGE;
				args = new Object[] {replyDTO.getMemberNum()}; 	//멤버 번호
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberNum : "+replyDTO.getMemberNum());
				return jdbcTemplate.queryForObject(query,  args, new CntRowMapper());
			}
			
			else {
				//컨디션값 오류
				System.err.println("log: Reply selectOne condition fail");
			}
			System.out.println("end");
		} catch (Exception e) {
			System.err.println("log: Reply selectOne Exception fail");
			return null;
		} 
		System.out.println("log: Reply selectAll return datas");
		return null;
	}
	class ReplyRowMapper implements RowMapper<ReplyDTO>{

		@Override
		public ReplyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReplyDTO data = new ReplyDTO();
			data.setReplyNum(rs.getInt("REPLY_NUM")); 						//댓글 번호
			data.setReplyContent(rs.getString("REPLY_CONTENT"));			//댓글 내용
			data.setMemberNum(rs.getInt("MEMBER_NUM"));						//멤버 번호
			data.setMemberNickname(rs.getString("MEMBER_NICKNAME"));		//작성자 닉네임
			data.setMemberProfileWay(rs.getString("MEMBER_PROFILE_WAY"));	//작성자 프로필
			data.setReplyWriteDay(rs.getString("REPLY_WRITE_DAY"));			//작성일시

			System.out.print(" | result "+data.getReplyNum());
			return data;
		}

	}
	class CntRowMapper implements RowMapper<ReplyDTO>{
		@Override
		public ReplyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReplyDTO data = new ReplyDTO();
			data.setCnt(rs.getInt("CNT"));
			System.out.print(" | result "+data.getCnt());
			return data;
		}
	}
}
