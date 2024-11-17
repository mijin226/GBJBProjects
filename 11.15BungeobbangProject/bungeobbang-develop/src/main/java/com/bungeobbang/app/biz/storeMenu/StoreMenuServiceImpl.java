package com.bungeobbang.app.biz.storeMenu;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//StoreMenuMenuService 구현체
@Service("storeMenuService")
public class StoreMenuServiceImpl implements StoreMenuService{
	@Autowired
	private StoreMenuDAO StoreMenuDAO;
    @Autowired
    private StoreMenuDAO storeMenuDAO;

	@Override
	public ArrayList<StoreMenuDTO> selectAll(StoreMenuDTO storeMenuDTO) {
		return null;
	}

	@Override
	public StoreMenuDTO selectOne(StoreMenuDTO storeMenuDTO) {
		return storeMenuDAO.selectOne(storeMenuDTO);
	}

	@Override
	public boolean insert(StoreMenuDTO storeMenuDTO) {
		return this.StoreMenuDAO.insert(storeMenuDTO);
	}

	@Override
	public boolean update(StoreMenuDTO storeMenuDTO) {
		return this.StoreMenuDAO.update(storeMenuDTO);
	}

	@Override
	public boolean delete(StoreMenuDTO storeMenuDTO) {
		//return this.StoreMenuDAO.delete(storeMenuDTO);
		return false;
	}
}
