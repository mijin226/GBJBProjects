package com.bungeobbang.app.biz.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LikeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//좋아요 추가
	//받은 데이터 : 게시물 고유번호(PK), 회원 고유번호(PK) 
	//추가 데이터 : 좋아요 고유번호(PK), 게시물 고유번호(PK), 회원 고유번호(PK) 
	private final String INSERT = "INSERT INTO BB_LIKE(BOARD_NUM, MEMBER_NUM) VALUES (?, ?)";
	//특정 회원의 특정 게시물 좋아요 여부 확인
	//받은 데이터 : 게시물 고유번호(PK), 회원 고유번호(PK)
	//조회 데이터 : 좋아요 고유번호(PK)
	private final String SELECTONE = "SELECT LIKE_NUM FROM BB_LIKE WHERE MEMBER_NUM = ? AND BOARD_NUM = ?";
	//좋아요 삭제
	//받은 데이터 : 좋아요 고유번호(PK)
	private final String DELETE = "DELETE FROM BB_LIKE WHERE LIKE_NUM = ?";
	private final String SELECTONE_CNT = "SELECT COUNT(*) AS CNT FROM BB_LIKE WHERE BOARD_NUM = ?";

	//좋아요 추가----------------------------------------------------------------------

	public boolean insert(LikeDTO likeDTO) {
		System.out.println("log_StoreLikeDAO_insert : start");
		System.out.println("log_StoreLikeDAO_insert_controller input StoreLikeDTO : " + likeDTO);
		int rs = jdbcTemplate.update(INSERT, likeDTO.getBoardNum(), likeDTO.getMemberNum());
		if (rs <= 0) { //rs >= 1(success) / rs = 0 (fail)
			System.err.println("log_LikeDAO_insert executeUpdate() fail : if(rs <= 0)");
			return false;
		}
		System.out.println("log_StoreLikeDAO insert true");
		return true;
	}

	private boolean update(LikeDTO likeDTO) {
		return false;
	}

	//좋아요 취소(삭제)
	public boolean delete(LikeDTO likeDTO) {
		System.out.println("log_StoreLikeDAO_delete : start");
		System.out.println("log_StoreLikeDAO_delete controller input likeDTO : " + likeDTO);
		int rs = jdbcTemplate.update(DELETE, likeDTO.getLikeNum());
		if (rs <= 0) {//rs >= 1(success) / rs = 0 (fail)
			System.err.println("log_LikeDTO_delete execute() fail");
			return false;
		}
		System.out.println("log LikeDTO delte success");
		return true;
	}
	private List<LikeDTO> selectAll(LikeDTO likeDTO) {
		return null;
	}
	//특정 회원의 특정 게시물 좋아요 여부 확인-----------------------------------------------------------------------------------
	public LikeDTO selectOne(LikeDTO likeDTO) {
		System.out.println("log_StoreLikeDAO_selectOne : start");
		System.out.println("log_StoreLikeDAO_selectOne controller input likeDTO : " + likeDTO);
		LikeDTO data;
		if(likeDTO.getCondition() != null && likeDTO.getCondition().equals("CNT")){
			Object[] args = new Object[]{likeDTO.getBoardNum()};
			data = jdbcTemplate.queryForObject(SELECTONE_CNT, args, (rs, i)->{
				LikeDTO dto = new LikeDTO();
				dto.setCnt(rs.getInt("CNT")); // 좋아요 고유번호
				return dto;
			});
		}
		else{
			Object[] args = {
					likeDTO.getMemberNum(),
					likeDTO.getBoardNum()
			};
			try {
				//쿼리 실행
				data = jdbcTemplate.queryForObject(SELECTONE, args, new LikeRowMapper());
				System.out.println("log_StoreLikeDAO_selectOne_executeQuery() complete");
			} catch (EmptyResultDataAccessException e) {
				data = null;
			}
		}
		return data;
	}
	class LikeRowMapper implements RowMapper<LikeDTO>{
		@Override
		public LikeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			LikeDTO data = new LikeDTO();
			data.setLikeNum(rs.getInt("LIKE_NUM")); // 좋아요 고유번호
			System.out.println("log_StoreLikeDAO_selectOne data : " + data.getLikeNum());
			return data;
		}
		
	}
}