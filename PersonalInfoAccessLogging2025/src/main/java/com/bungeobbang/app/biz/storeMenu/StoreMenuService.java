package com.bungeobbang.app.biz.storeMenu;

import java.util.ArrayList;

public interface StoreMenuService {
	ArrayList<StoreMenuDTO> selectAll(StoreMenuDTO storeMenuDTO);
	StoreMenuDTO selectOne(StoreMenuDTO storeMenuDTO);
	boolean insert(StoreMenuDTO storeMenuDTO);
	boolean update(StoreMenuDTO storeMenuDTO);
	boolean delete(StoreMenuDTO storeMenuDTO);
}
