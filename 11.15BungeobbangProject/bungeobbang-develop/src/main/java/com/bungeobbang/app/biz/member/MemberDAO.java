package com.bungeobbang.app.biz.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bungeobbang.app.biz.filterSearch.MemberFilter;

@Repository
public class MemberDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// INSERT 쿼리
	private final String INSERT = "INSERT INTO BB_MEMBER( "
			+ "MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_PHONE, "
			+ "MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

	// UPDATE 쿼리들
	private final String UPDATE = "UPDATE BB_MEMBER SET MEMBER_NAME = ?, "
			+ "MEMBER_PHONE = ?, MEMBER_NICKNAME = ?, MEMBER_PROFILE_WAY = ? "
			+ "WHERE MEMBER_NUM = ?";
	private final String UPDATE_PASSWORD = "UPDATE BB_MEMBER SET MEMBER_PASSWORD = ? WHERE MEMBER_NUM = ?";

	// DELETE 쿼리
	private final String DELETE = "DELETE FROM BB_MEMBER WHERE MEMBER_NUM = ?";

	// SELECTALL 쿼리(selectAll 현재 구현 X)
	// @rownum : 세션별로 사용되는 MySQL 변수(번호 부여)
	private final String SELECTALL = "SELECT @rownum := @rownum + 1 AS RN, MEMBER_NUM, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PHONE, MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY "
			+ "FROM (SELECT MEMBER_NUM, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PHONE, MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY, MEMBER_POINT "
			+ "FROM BB_MEMBER "
			+ "WHERE 1=1 ";

	private final String SELECTALL_ENDPART = " ORDER BY MEMBER_HIREDAY DESC LIMIT ?, ?)"
			+ "AS SUBQUERY , (SELECT @rownum :=0) AS R";
	// (SELECT @rownum :=0) AS R : rownum을 0으로 초기화하는 역할 -> 정확도 향상
	// MySQL에서는 서브쿼리나 테이블을 사용할 때 항상 별칭을 붙여야 함 


	// SELECTALL_RECENT 쿼리
	private final String SELECTALL_RECENT = "SELECT @rownum := @rownum + 1 AS RN, "
			+ "       MEMBER_NUM, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PHONE, \r\n"
			+ "       MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY "
			+ "FROM ("
			+ "    SELECT MEMBER_NUM, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PHONE, "
			+ "           MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY"
			+ "    FROM BB_MEMBER\r\n"
			+ "    WHERE MEMBER_HIREDAY >= NOW() - INTERVAL ? DAY"
			+ ") AS SUBQUERY, (SELECT @rownum := 0) AS R"
			+ "LIMIT ?, ?";

	// SELECTONE 쿼리들
	private final String SELECTONE_EMAIL = "SELECT MEMBER_EMAIL FROM BB_MEMBER WHERE MEMBER_EMAIL = ?";
	private final String SELECTONE_NICKNAME = "SELECT MEMBER_NICKNAME FROM BB_MEMBER WHERE MEMBER_NICKNAME = ?";
	private final String SELECTONE_PASSWORD_RESET = "SELECT MEMBER_NUM FROM BB_MEMBER WHERE MEMBER_EMAIL = ? AND MEMBER_NAME = ?";
	private final String SELECTONE_LOGIN = "SELECT MEMBER_NUM, MEMBER_NICKNAME, MEMBER_ROLE, MEMBER_PROFILE_WAY FROM BB_MEMBER WHERE MEMBER_EMAIL = ? AND MEMBER_PASSWORD = ?";
	private final String SELECTONE_INFO = "SELECT MEMBER_NUM, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PHONE, MEMBER_NICKNAME, MEMBER_PROFILE_WAY, MEMBER_ROLE, MEMBER_HIREDAY "
			+ "FROM BB_MEMBER WHERE MEMBER_NUM = ?";
	private final String SELECTONE_PASSWORD_CHECK = "SELECT MEMBER_NUM FROM BB_MEMBER WHERE MEMBER_NUM = ? AND MEMBER_PASSWORD = ?";
	private final String SELECTONE_PROFILE = "SELECT MEMBER_PROFILE_WAY FROM BB_MEMBER WHERE MEMBER_NUM = ?";

	//고정설정
	private final int RECENT_PIVOT = 7; //최근가입한 회원 기준 (day)

	public boolean insert(MemberDTO memberDTO) {
		//회원가입
		System.out.println("log: Member insert start");
		int rs = 0;
		try {
			rs = jdbcTemplate.update(INSERT,
					memberDTO.getMemberEmail(),	//이메일
					memberDTO.getMemberPassword(), 	//비밀번호
					memberDTO.getMemberName(), 		//이름
					memberDTO.getMemberPhone(),	//전화번호
					memberDTO.getMemberNickname(),	//닉네임
					memberDTO.getMemberProfileWay(), //프로필사진경로
					memberDTO.getMemberRole(),
					memberDTO.getMemberHireDay()
					); 		//권한
			//넘어온 값 확인 로그
			System.out.println("log: parameter MemberDTO : "+memberDTO);

		}catch (Exception e) {
			System.err.println("log: Member insert Exception fail");
			e.printStackTrace();
			return false;
		}
		if(rs<=0) {
			System.err.println("Member insert fail");
			return false;
		}
		System.out.println("Member insert success");
		return true;
	}

	public boolean update(MemberDTO memberDTO) {
		//회원 정보 수정
		System.out.println("log: Member update start");
		int rs = 0;
		try {
			if(memberDTO.getCondition().equals("UPDATE_CONDITION")) {
				//개인정보수정(비밀번호 제외)
				rs = jdbcTemplate.update(UPDATE,
						memberDTO.getMemberName(), 		//이름
						memberDTO.getMemberPhone(), 	//전화번호
						memberDTO.getMemberNickname(), 	//닉네임
						memberDTO.getMemberProfileWay(), //프로필사진경로
						memberDTO.getMemberNum()
						);//멤버 번호
				//넘어온 값 확인 로그
				System.out.println("log: parameter MemberDTO : "+memberDTO);
			}
			else if(memberDTO.getCondition().equals("UPDATE_PASSWORD_CONDTION")) {
				//개인정보수정 비밀번호
				System.out.println("log: Member update : UPDATE_PASSWORD");
				rs = jdbcTemplate.update(UPDATE_PASSWORD,
						memberDTO.getMemberPassword(),
						memberDTO.getMemberNum()
						); 			//멤버 번호
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberPassword : "+memberDTO.getMemberPassword());
				System.out.println("log: parameter getMemberNum : "+memberDTO.getMemberNum());
			}
			else {
				//컨디션값 오류
				System.err.println("log: Member update condition fail");
			}
			if(rs<=0) {
				System.err.println("log : Member update fail");
				return false;
			}
		} catch (Exception e) {
			System.err.println("log: Member update Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Member update success");
		return true;
	}

	public boolean delete(MemberDTO memberDTO) {
		System.out.println("log: Member delete start");
		int rs = 0;
		try {
			rs = jdbcTemplate.update(DELETE,
					memberDTO.getMemberNum()
					);
			//넘어온 값 확인 로그
			System.out.println("log: parameter getMemberNum : "+memberDTO.getMemberNum());
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Member delete execute fail");
				return false;
			}
		}  catch (Exception e) {
			System.err.println("log: Member delete Exception fail");
			return false;
		} 
		System.out.println("log : Member delete success");
		return true;
	}

	private List<MemberDTO> selectAll(MemberDTO memberDTO) {
		System.out.println("log: Member selectAll start");
		List<MemberDTO> datas = new ArrayList<MemberDTO>();
		String query = "";
		Object[] args = null;

		if(memberDTO.getCondition().equals("ALL_LIST_CONDITON")) {
			//전체회원(+필터검색)
			System.out.println("log: Member selectAll : SELECTALL");
			//필터검색 추가
			HashMap<String, String> filters = memberDTO.getFilterList();//넘어온 MAP filter키워드
			//키워드를 담을 객체
			List<Object> argsList = new ArrayList<>();
			MemberFilter filterUtil = new MemberFilter();
			// 쿼리문 생성
			query = filterUtil.buildFilterQuery(SELECTALL,filters).append(" "+SELECTALL_ENDPART).toString();

			argsList = filterUtil.setFilterKeywords(argsList, filters); 		//필터 검색 검색어 
			argsList.add(memberDTO.getStartNum());	//페이지네이션 용 시작번호
			argsList.add(memberDTO.getEndNum());		//페이지네이션 용 끝번호
			//넘어온 값 확인 로그
			System.out.println("log: parameter getStartNum : "+memberDTO.getStartNum());
			System.out.println("log: parameter getEndNum : "+memberDTO.getEndNum());
			//args 배열화
			args = argsList.toArray();
		}
		else if(memberDTO.getCondition().equals("RECENT_LIST_CONDITON")) {
			//신규회원
			System.out.println("log: Member selectAll : SELECTALL_RECENT");
			//쿼리문 생성
			query = SELECTALL_RECENT;
			args = new Object[]{
					RECENT_PIVOT,
					memberDTO.getStartNum(),
					memberDTO.getEndNum()
			};
			//넘어온 값 확인 로그
			System.out.println("log: parameter getStartNum : "+memberDTO.getStartNum());
			System.out.println("log: parameter getEndNum : "+memberDTO.getEndNum());
		}
		else {
			//컨디션값 오류
			System.err.println("log: Member selectAll condition fail");
		}
		try {
			datas = jdbcTemplate.query(query, args, new AllRowMapper());					
		}catch(Exception e) {
			System.err.println("log : Member selectAll fail");
			e.printStackTrace();
			return null;
		}
		System.out.println("Member selectAll success");
		return datas;
	}

	public MemberDTO selectOne(MemberDTO memberDTO) {
		System.out.println("log: Member selectOne start");
		MemberDTO data = null;
		String query ="";
		Object[] args = null;
		try {
			if(memberDTO.getCondition().equals("EMAIL_CONDITION")) {
				//이메일 중복조회
				System.out.println("log: Member selectOne : SELECTONE_EMAIL");
				query = SELECTONE_EMAIL;
				// 키워드를 담을 리스트
				args = new Object[] {
						memberDTO.getMemberEmail()
				};
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberEmail : "+memberDTO.getMemberEmail());

                try {
                    data = jdbcTemplate.queryForObject(query, args, new EmailRowMapper());
                } catch (EmptyResultDataAccessException e) {
                    data = null;
                }

            }
			else if(memberDTO.getCondition().equals("NICKNAME_CONDITION")) {
				//닉네임 중복조회
				System.out.println("log: Member selectOne : SELECTONE_NICKNAME");
				query = SELECTONE_NICKNAME;
				args = new Object[] {
						memberDTO.getMemberNickname() //닉네임
				};
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberNickname : "+memberDTO.getMemberNickname());

                try {
                    data = jdbcTemplate.queryForObject(query, args, new NickRowMapper());
                } catch (EmptyResultDataAccessException e) {
                    data = null;
                }
            }
			else if(memberDTO.getCondition().equals("PASSWORD_RESET_CONDITION")) {
				//비밀번호 리셋 
				System.out.println("log: Member selectOne : SELECTONE_PASSWORD_RESET");
				query = SELECTONE_PASSWORD_RESET;
				args = new Object[] {
						memberDTO.getMemberEmail(), //이메일
						memberDTO.getMemberName()  //이름
				};
				
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberEmail : "+memberDTO.getMemberEmail());
				System.out.println("log: parameter getMemberName : "+memberDTO.getMemberName());

                try {
                    data = jdbcTemplate.queryForObject(query,args,  new NumRowMapper());
                } catch (EmptyResultDataAccessException e) {
                    data = null;
                }

            }
			else if(memberDTO.getCondition().equals("LOGIN_CONDITON")) {
				//로그인
				System.out.println("log: Member selectOne : SELECTONE_LOGIN");
				query= SELECTONE_LOGIN;
				args = new Object[] {
						memberDTO.getMemberEmail(),
						memberDTO.getMemberPassword()
				};
				
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberEmail : "+memberDTO.getMemberEmail());
				System.out.println("log: parameter getMemberPassword : "+memberDTO.getMemberPassword());

                try {
                    data = jdbcTemplate.queryForObject(query,args,new LoginRowMapper());
                } catch (EmptyResultDataAccessException e) {
                    data = null;
                }
            }
			else if(memberDTO.getCondition().equals("INFO_CONDITION")) {
				//회원정보
				System.out.println("log: Member selectOne : SELECTONE_INFO");
				query = SELECTONE_INFO;
				args = new Object[] {
						memberDTO.getMemberNum()
				};
				
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberNum : "+memberDTO.getMemberNum());
				
				data = jdbcTemplate.queryForObject(query, args, new AllRowMapper());
			}
			else if(memberDTO.getCondition().equals("PASSWORD_CHECK_CONDITION")) {
				//패스워드 확인
				System.out.println("log: Member selectOne : SELECTONE_PASSWORD_CHECK");
				query = SELECTONE_PASSWORD_CHECK;
				args = new Object[] {
						memberDTO.getMemberNum(),
						memberDTO.getMemberPassword()
				};
				
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberNum : "+memberDTO.getMemberNum());
				System.out.println("log: parameter getMemberPassword : "+memberDTO.getMemberPassword());
				
				data = jdbcTemplate.queryForObject(query, args, new NumRowMapper());
				
			}
			else if(memberDTO.getCondition().equals("PROFILE_WAY_CONDITION")) {
				//프로필이미지경로
				System.out.println("log: Member selectOne : SELECTONE_PROFILE");
				query = SELECTONE_PROFILE;
				args = new Object[] {
						memberDTO.getMemberNum()
				};
				//넘어온 값 확인 로그
				System.out.println("log: parameter getMemberNum : "+memberDTO.getMemberNum());
				data = jdbcTemplate.queryForObject(query,args, new ProfileRowMapper());

			}
//			else if(memberDTO.getCondition().equals("CNT_CONDITION")) {
//				//전체회원 수 (+필터검색)
//				System.out.println("log: Member selectOne : SELECTONE_CNT");
//				//필터검색 추가
//				HashMap<String, String> filters = memberDTO.getFilterList();//넘어온 MAP filter키워드
//				MemberFilter filterUtil= new MemberFilter();
//				// 키워드를 담을 리스트
//				List<Object> argsList = new ArrayList<>();
//				query = filterUtil.buildFilterQuery(SELECTONE_CNT,filters).toString();
//
//				argsList = filterUtil.setFilterKeywords(argsList,filters); 		//필터 검색 검색어
//				// args 배열화
//				args = argsList.toArray();
//
//				data = jdbcTemplate.queryForObject(query, args, new CntRowMapper());
//
//			}
//			else if(memberDTO.getCondition().equals("RECENT_CONDITION")) {
//				//최신 회원의 수
//				System.out.println("log: Member selectOne : SELECTONE_RECENT");
//				query = SELECTONE_RECENT;
//				args = new Object[] {
//						RECENT_PIVOT
//				};
//				data = jdbcTemplate.queryForObject(query, args, new CntRowMapper());
//			}
			else {
				//컨디션값 오류
				System.err.println("log: Member selectOne condition fail");
			}
			System.out.println("end");
		}catch (Exception e) {
			System.err.println("log: Member selectOne Exception fail");
			e.printStackTrace();
			return null;
		} 
		return data;
	}
	class AllRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberName(rs.getString("MEMBER_NAME")); 				//이름
			data.setMemberEmail(rs.getString("MEMBER_EMAIL")); 				//이메일
			data.setMemberNickname(rs.getString("MEMBER_NICKNAME")); 		//닉네임
			data.setMemberPhone(rs.getString("MEMBER_PHONE")); 				//전화번호
			data.setMemberProfileWay(rs.getString("MEMBER_PROFILE_WAY")); 	//프로필 사진 경로
			data.setMemberHireDay(rs.getString("MEMBER_HIREDAY")); 			//가입일자
			//반환된 객체 리스트에 추가
			System.out.print(" | result "+data);
			return data;
		}

	}
	class EmailRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberEmail(rs.getString("MEMBER_EMAIL")); //이메일
			System.out.println("result exists");
			return data;
		}		
	}
	class NickRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberNickname(rs.getString("MEMBER_NICKNAME")); //닉네임
			System.out.println("reslut exists");
			return data;
		}
		
	}
	class NumRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberNum(rs.getInt("MEMBER_NUM")); //멤버번호
			System.out.println("result exists");
			return data;
		}
	}
	class LoginRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberNum(rs.getInt("MEMBER_NUM")); 			//멤버번호
			data.setMemberNickname(rs.getString("MEMBER_NICKNAME")); //닉네임
			data.setMemberRole(rs.getString("MEMBER_ROLE")); 		//권한
			data.setMemberProfileWay(rs.getString("MEMBER_PROFILE_WAY")); 		//권한
			System.out.println("result exists");
			return data;
		}
		
	}
	class ProfileRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setMemberProfileWay(rs.getString("MEMBER_PROFILE_WAY")); 	//프로필 사진 경로
			System.out.println("result exists");
			return data;
		}
		
	}
	class CntRowMapper implements RowMapper<MemberDTO>{

		@Override
		public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberDTO data = new MemberDTO();
			data.setCnt(rs.getInt("CNT"));
			return data;
		}
		
	}
}
