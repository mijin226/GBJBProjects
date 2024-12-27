package com.bungeobbang.app.biz.product;

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

import com.bungeobbang.app.biz.filterSearch.ProductFilter;


@Repository
public class ProductDAO{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INSERT_CRAWLING = "INSERT INTO BB_PRODUCT(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_PROFILE_WAY, PRODUCT_CATEGORY_NUM) "
			+ "VALUES (?, ?, ?, ?)";
	private final String UPDATE = "UPDATE BB_PRODUCT SET PRODUCT_NAME = ?, PRODUCT_PRICE = ?, PRODUCT_PROFILE_WAY = ?, PRODUCT_CATEGORY_NUM = ? WHERE PRODUCT_NUM = ?";
	private final String DELETE = "DELETE FROM BB_PRODUCT WHERE PRODUCT_NUM = ?";

	private final String SELECTALL = "SELECT PRODUCT_NUM, PRODUCT_NAME, PRODUCT_PRICE, "
			+ "PRODUCT_PROFILE_WAY,PRODUCT_CATEGORY_NUM, PRODUCT_CATEGORY_NAME, "
			+ "BOARD_NUM "
			+"FROM BB_VIEW_PRODUCT_JOIN "
			+"WHERE 1=1 ";

	private final String SELECTALL_ENDPART = "ORDER BY PRODUCT_NUM DESC LIMIT ?, ?";

	private final String SELECTONE = "SELECT PRODUCT_NUM, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_PROFILE_WAY, "
			+ "BOARD_NUM, PRODUCT_CATEGORY_NUM, PRODUCT_CATEGORY_NAME, BOARD_TITLE, BOARD_CONTENT "
			+ "FROM BB_VIEW_PRODUCT_JOIN "
			+ "WHERE PRODUCT_NUM = ?";

	private final String SELECTONE_CNT = "SELECT COUNT(*) AS CNT FROM BB_PRODUCT bp "
			+ "LEFT JOIN BB_BOARD bb ON bp.PRODUCT_NUM = bb.PRODUCT_NUM WHERE 1=1 ";


	public boolean insert(ProductDTO productDTO) {
		System.out.println("log: Product insert start");
		int rs = 0;
		try {
			if(productDTO.getCondition().equals("CRAWLING_ONLY")) {
				System.out.println("log: Product insert condition : CRAWLING_ONLY");
				//오직 크롤링 insert용 (Controller에서 사용하지 않음 / Crawling insert (Listener)는 model에서 담당)

				//넘어온 값 확인 로그
				System.out.println("log: Product insert productDTO : "+productDTO);

				rs = jdbcTemplate.update(INSERT_CRAWLING,
						productDTO.getProductName(), 		//상품 이름
						productDTO.getProductPrice(), 			//상품 가격
						productDTO.getProductProfileWay(),	//썸네일
						productDTO.getProductCategoryNum() //상품 카테고리 번호
						); 	
			}
//			else {				
//				//새 상품작성
//				System.out.println("log: parameter getProductName : "+productDTO.getProductName());
//				System.out.println("log: parameter getProductPrice : "+productDTO.getProductPrice());
//				System.out.println("log: parameter getProductProfileWay : "+productDTO.getProductProfileWay());
//				System.out.println("log: parameter getProductCateNum : "+productDTO.getProductCategoryNum());
//				rs = jdbcTemplate.update(INSERT,
//						productDTO.getProductName(), 		//상품 이름
//						productDTO.getProductPrice(), 			//상품 가격
//						productDTO.getProductProfileWay(), 	//썸네일
//						productDTO.getProductCategoryNum()
//						); 		//상품 카테고리 번호
//				//넘어온 값 확인 로그
//			}
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Product insert execute fail");
				return false;
			}
		} catch (Exception e) {
			System.err.println("log: Product insert Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Product insert true");
		return true;
	}

	private boolean update(ProductDTO productDTO) {
		System.out.println("log: Product update start");
		int rs=0;
		//상품 수정
		System.out.println("log: Product update : UPDATE");
		System.out.println("log: parameter getProductName : "+productDTO.getProductName());
		System.out.println("log: parameter getProductPrice : "+productDTO.getProductPrice());
		System.out.println("log: parameter getProductProfileWay : "+productDTO.getProductProfileWay());
		System.out.println("log: parameter getProductCategoryNum : "+productDTO.getProductCategoryNum());
		System.out.println("log: parameter getProductNum : "+productDTO.getProductNum());
		try {
			rs=jdbcTemplate.update(UPDATE,
					productDTO.getProductName(), 		//상품 이름
					productDTO.getProductPrice(), 			//상품 가격
					productDTO.getProductProfileWay(),	//썸네일
					productDTO.getProductCategoryNum(), 		//상품 카테고리 번호
					productDTO.getProductNum()
					); 			//상품 번호
			//넘어온 값 확인 로그
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Product update execute fail");
				return false;
			}
		} catch (Exception e) {
			System.err.println("log: Product update Exception fail");
			e.printStackTrace();
			return false;
		}
		System.out.println("log: Product update true");
		return true;
	}

	private boolean delete(ProductDTO productDTO) {
		System.out.println("log: Product delete start");
		int rs=0;
		try {
			System.out.println("log: parameter getProductNum : "+productDTO.getProductNum());
			jdbcTemplate.update(DELETE,
					productDTO.getProductNum()
					); //상품 번호
			//넘어온 값 확인 로그
			if(rs <= 0) { 
				//쿼리는 정상적으로 실행됐으나 실패
				System.err.println("log: Product delete execute fail");
				return false;
			}
		}catch (Exception e) {
			System.err.println("log: Product delete Exception fail");
			e.printStackTrace();
			return false;
		} 
		System.out.println("log: Product delete true");
		return true;
	}

	public List<ProductDTO> selectAll(ProductDTO productDTO) {
		System.out.println("log: Product selectAll start");
		List<ProductDTO> datas = new ArrayList<>();
		String query = "";
		Object[] args = null;
		//상품리스트 (+필터검색)
		//필터검색 추가

		HashMap<String, String> filters = productDTO.getFilterList();//넘어온 MAP filter키워드
		ProductFilter filterUtil = new ProductFilter();
		query = filterUtil.buildFilterQuery(SELECTALL,filters).append(" "+SELECTALL_ENDPART).toString();
		//필터검색 리스트
		List<Object> argsList = new ArrayList<>();
		argsList = filterUtil.setFilterKeywords(argsList,filters); 		//필터 검색 검색어 

		argsList.add(productDTO.getStartNum() < 1 ? 0 : productDTO.getStartNum() - 1);
		argsList.add(productDTO.getEndNum());				//페이지네이션 용 끝번호
		//넘어온 값 확인 로그
		System.out.println("log: parameter getStartNum : "+productDTO.getStartNum());
		System.out.println("log: parameter getEndNum : "+productDTO.getEndNum());

		args = argsList.toArray();
		System.out.println("log : Product selectAll query start : "+query);
		System.out.println("log : Product selectAll args ="+Arrays.toString(args));
		try {
			datas = jdbcTemplate.query(query, args, new  ProductRowMapper());
			System.out.println("end");
		}catch (Exception e) {
			System.err.println("log: Product selectAll Exception fail");
			e.printStackTrace();
			return null;
		} 
		System.out.println("log: Product selectAll return datas");
		return datas;
	}

	public ProductDTO selectOne(ProductDTO productDTO) {
		System.out.println("log: Product selectOne start");
		ProductDTO data = null;
		String query ="";
		Object[] args=null;
		if(productDTO.getCondition().equals("MD_ONE")) {
			//넘어온 값 확인 로그
			System.out.println("log: parameter getProductNum : "+productDTO.getProductNum());
			//상품 상세보기
			System.out.println("log: Product selectOne : MD_ONE");
			query=SELECTONE;
			args=new Object[] {
					productDTO.getProductNum() 	//상품번호
			};
			try {
				data = jdbcTemplate.queryForObject(query, args, new MDRowMapper());
			}
			catch(Exception e) {
				System.err.println("log : Product selectOne MD_ONE fail");
				e.printStackTrace();
				return null;
			}

		}
		else if(productDTO.getCondition().equals("FILTER_CNT")) {
			//검색 별 상품 개수(+필터검색)
			System.out.println("log: Product selectOne : FILTER_CNT");
			//필터검색 추가
			HashMap<String, String> filters = productDTO.getFilterList();//넘어온 MAP filter키워드
			ProductFilter filterUtil = new ProductFilter();
			query = filterUtil.buildFilterQuery(SELECTONE_CNT,filters).toString();

			List<Object> argsList = new ArrayList<>();			
			argsList = filterUtil.setFilterKeywords(argsList, filters); 		//필터 검색 검색어 

			args = argsList.toArray();
			try {
				data = jdbcTemplate.queryForObject(query, args, new CntRowMapper());
			}
			catch(Exception e) {
				System.err.println("log : Product selectOne : FILTER_CNT fail");
				e.printStackTrace();
				return null;
			}
		}
		else {
			//컨디션값 오류
			System.err.println("log: Product selectOne condition fail");
		}
		System.out.println("log: Product selectAll return datas");
		return data;
	}
	class ProductRowMapper implements RowMapper<ProductDTO>{

		@Override
		public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductDTO data = new ProductDTO();
			data.setProductNum(rs.getInt("PRODUCT_NUM"));		//상품번호
			data.setProductName(rs.getString("PRODUCT_NAME"));	//상품이름
			data.setProductPrice(rs.getInt("PRODUCT_PRICE"));	//상품가격
			data.setProductProfileWay(rs.getString("PRODUCT_PROFILE_WAY"));	//썸네일
			data.setProductCategoryNum(rs.getInt("PRODUCT_CATEGORY_NUM"));	//상품카테고리번호
			data.setProductCategoryName(rs.getString("PRODUCT_CATEGORY_NAME"));	//상품카테고리명
			//반환된 객체 리스트에 추가
			System.out.print(" | result "+data.getProductNum());
			return data;
		}
	}

	class MDRowMapper implements RowMapper<ProductDTO>{

		@Override
		public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductDTO data = new ProductDTO();
			data.setProductNum(rs.getInt("PRODUCT_NUM"));				//상품번호
			data.setProductName(rs.getString("PRODUCT_NAME"));			//상품이름
			data.setProductPrice(rs.getInt("PRODUCT_PRICE"));			//상품가격
			data.setProductProfileWay(rs.getString("PRODUCT_PROFILE_WAY"));	//썸네일
			data.setProductCategoryName(rs.getString("PRODUCT_CATEGORY_NAME"));	//상품카테고리명
			data.setProductCategoryNum(rs.getInt("PRODUCT_CATEGORY_NUM"));	//상품카테고리번호
			data.setBoardNum(rs.getInt("BOARD_NUM"));					//상품 게시글 번호
			data.setBoardTitle(rs.getString("BOARD_TITLE"));			//상품 게시글 제목
			data.setBoardContent(rs.getString("BOARD_CONTENT"));		//상품 게시글 내용
			System.out.println("result exists");
			return data;
		}
	}
	class CntRowMapper implements RowMapper<ProductDTO>{

		@Override
		public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductDTO data = new ProductDTO();
			data.setCnt(rs.getInt("CNT")); 				//상품 수
			System.out.println("result exists");
			return data;
		}

	}
}