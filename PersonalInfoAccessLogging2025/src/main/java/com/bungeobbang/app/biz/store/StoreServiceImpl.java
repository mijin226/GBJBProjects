package com.bungeobbang.app.biz.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//StoreService 구현체
@Service("storeService")
public class StoreServiceImpl implements StoreService{
	@Autowired
	private StoreDAO storeDAO;

	@Override
	public ArrayList<StoreDTO> selectAll(StoreDTO storeDTO) {
		return (ArrayList<StoreDTO>) this.storeDAO.selectAll(storeDTO);
	}

	@Override
	public StoreDTO selectOne(StoreDTO storeDTO) {
		return this.storeDAO.selectOne(storeDTO);
	}

	@Override
	public boolean insert(StoreDTO storeDTO) {
		return this.storeDAO.insert(storeDTO);
	}

	@Override
	public boolean update(StoreDTO storeDTO) {
		return this.storeDAO.update(storeDTO);
	}

	@Override
	public boolean delete(StoreDTO storeDTO) {
		return this.storeDAO.delete(storeDTO);
	}
	
}
