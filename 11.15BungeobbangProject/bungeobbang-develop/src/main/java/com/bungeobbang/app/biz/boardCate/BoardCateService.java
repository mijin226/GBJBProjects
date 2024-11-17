package com.bungeobbang.app.biz.boardCate;

import java.util.ArrayList;

public interface BoardCateService {
	ArrayList<BoardCateDTO> selectAll(BoardCateDTO boardCateDTO);
	BoardCateDTO selectOne(BoardCateDTO boardCateDTO);
	boolean insert(BoardCateDTO boardCateDTO);
	boolean update(BoardCateDTO boardCateDTO);
	boolean delete(BoardCateDTO boardCateDTO);
}
