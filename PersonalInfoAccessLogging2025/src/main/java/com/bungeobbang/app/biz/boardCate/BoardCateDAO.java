package com.bungeobbang.app.biz.boardCate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BoardCateDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String INSERT = "INSERT INTO BB_BOARD_CATEGORY (BOARD_CATEGORY_NAME) VALUES (?)";
    private final String UPDATE = "UPDATE BB_BOARD_CATEGORY SET BOARD_CATEGORY_NAME = ? WHERE BOARD_CATEGORY_NUM = ?";
    private final String DELETE = "DELETE FROM BB_BOARD_CATEGORY WHERE BOARD_CATEGORY_NUM = ?";
    private final String SELECTALL = "SELECT BOARD_CATEGORY_NUM, BOARD_CATEGORY_NAME FROM BB_BOARD_CATEGORY ORDER BY BOARD_CATEGORY_NUM";
    private final String SELECTONE = "SELECT BOARD_CATEGORY_NUM FROM BB_BOARD_CATEGORY where BOARD_CATEGORY_NAME = ?";

    public boolean insert(BoardCateDTO boardCateDTO) {
        System.out.println("Insert BoardCateDTO getBoardCateName: ["+ boardCateDTO.getBoardCategoryName()+"]");
        return jdbcTemplate.update(INSERT, boardCateDTO.getBoardCategoryName()) > 0;
    }

    public boolean update(BoardCateDTO boardCateDTO) {
    	System.out.println("Update BoardCateDTO getBoardCateName : [{"+ boardCateDTO.getBoardCategoryName()+"}] "
    			+ "getBoardCateNum : [{"+boardCateDTO.getBoardCategoryNum()+"}]");
        return jdbcTemplate.update(UPDATE, boardCateDTO.getBoardCategoryName(), boardCateDTO.getBoardCategoryNum()) > 0;
    }

    public boolean delete(BoardCateDTO boardCateDTO) {
        System.out.println("Delete BoardCateDTO getBoardCateNum : [{"+boardCateDTO.getBoardCategoryNum()+"}]");
        return jdbcTemplate.update(DELETE, boardCateDTO.getBoardCategoryNum()) > 0;
    }

    public List<BoardCateDTO> selectAll(BoardCateDTO boardCateDTO) {
        //Object[] args = {};넣을 값이 없으므로 생략
        //반환타입이 List
        return jdbcTemplate.query(SELECTALL, new BoardCateMapper()); //사용 안함
    }

    public BoardCateDTO selectOne(BoardCateDTO boardCateDTO) {
        System.out.println("selectOne BoardCateDTO ");
        Object[] args = {boardCateDTO.getBoardCategoryName()};
        return jdbcTemplate.queryForObject(SELECTONE, args, new BoardCateMapper());
    }
}

//반환 타입 설정
class BoardCateMapper implements RowMapper<BoardCateDTO> {
    @Override
    public BoardCateDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        BoardCateDTO data = new BoardCateDTO();
        data.setBoardCategoryNum(resultSet.getInt("BOARD_CATEGORY_NUM"));
        return data;
    }
}
