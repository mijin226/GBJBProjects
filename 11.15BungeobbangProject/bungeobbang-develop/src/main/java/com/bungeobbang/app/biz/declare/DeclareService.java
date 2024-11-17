package com.bungeobbang.app.biz.declare;

import java.util.ArrayList;

public interface DeclareService {
	boolean insert(DeclareDTO declareDTO);
	boolean update(DeclareDTO declareDTO);
	boolean delete(DeclareDTO declareDTO);
	ArrayList<DeclareDTO> selectAll(DeclareDTO declareDTO);
	DeclareDTO selectOne(DeclareDTO declareDTO);
}
