package com.bungeobbang.app.biz.storeWork;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//StoreWorkService 구현체
@Service("storeWorkService")
public class StoreWorkServiceimpl implements StoreWorkService{
	@Autowired
	private StoreWorkDAO storeWorkDAO;
	
	@Override
	public ArrayList<StoreWorkDTO> selectAll(StoreWorkDTO storeWorkDTO) {
		return this.storeWorkDAO.selectAll(storeWorkDTO);
	}

	@Override
	public StoreWorkDTO selectOne(StoreWorkDTO storeWorkDTO) {
		return storeWorkDTO;
	}

	@Override
	public boolean insert(StoreWorkDTO storeWorkDTO) {
		return this.storeWorkDAO.insert(storeWorkDTO);
	}

	@Override
	public boolean update(StoreWorkDTO storeWorkDTO) {
		return this.storeWorkDAO.update(storeWorkDTO);
	}

	@Override
	public boolean delete(StoreWorkDTO storeWorkDTO) {
		return false;
	}

}
