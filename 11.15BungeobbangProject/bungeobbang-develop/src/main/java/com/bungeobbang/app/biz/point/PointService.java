package com.bungeobbang.app.biz.point;

import java.util.ArrayList;

public interface PointService {
	boolean insert(PointDTO pointDTO);
	boolean update(PointDTO pointDTO);
	boolean delete(PointDTO pointDTO);
	ArrayList<PointDTO> selectAll(PointDTO pointDTO);
	PointDTO selectOne(PointDTO pointDTO);
}
