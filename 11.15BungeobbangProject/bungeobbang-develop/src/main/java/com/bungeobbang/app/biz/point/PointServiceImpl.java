package com.bungeobbang.app.biz.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("pointService")
public class PointServiceImpl implements PointService{
	
	@Autowired
	private PointDAO pointDAO;
	
	@Override
	public boolean insert(PointDTO pointDTO) {
		return false;
	}

	@Override
	public boolean update(PointDTO pointDTO) {
		return false;
	}

	@Override
	public boolean delete(PointDTO pointDTO) {
		return false;
	}

	@Override
	public ArrayList<PointDTO> selectAll(PointDTO pointDTO) {
		return (ArrayList<PointDTO>) this.pointDAO.selectAll(pointDTO);
	}

	@Override
	public PointDTO selectOne(PointDTO pointDTO) {
		return this.pointDAO.selectOne(pointDTO);
	}

}
