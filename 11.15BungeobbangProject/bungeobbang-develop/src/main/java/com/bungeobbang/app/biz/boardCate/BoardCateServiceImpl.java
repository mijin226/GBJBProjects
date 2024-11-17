package com.bungeobbang.app.biz.boardCate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// boardCate의 구현체
@Service("boardCateService")
public class BoardCateServiceImpl implements BoardCateService {
	@Autowired
	private BoardCateDAO boardCateDAO;
	
	@Override
	public ArrayList<BoardCateDTO> selectAll(BoardCateDTO boardCateDTO) {
		return (ArrayList<BoardCateDTO>) this.boardCateDAO.selectAll(boardCateDTO);
	}

	@Override
	public BoardCateDTO selectOne(BoardCateDTO boardCateDTO) {
		return this.boardCateDAO.selectOne(boardCateDTO);
	}

	@Override
	public boolean insert(BoardCateDTO boardCateDTO) {
		return this.boardCateDAO.insert(boardCateDTO);
	}

	@Override
	public boolean update(BoardCateDTO boardCateDTO) {
		return this.boardCateDAO.update(boardCateDTO);
	}

	@Override
	public boolean delete(BoardCateDTO boardCateDTO) {
		return this.boardCateDAO.delete(boardCateDTO);
	}

}
