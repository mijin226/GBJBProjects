package com.bungeobbang.app.biz.storeWork;

import java.util.ArrayList;

public interface StoreWorkService {
	ArrayList<StoreWorkDTO> selectAll(StoreWorkDTO storeWorkDTO);
	StoreWorkDTO selectOne(StoreWorkDTO storeWorkDTO);
	boolean insert(StoreWorkDTO storeWorkDTO);
	boolean update(StoreWorkDTO storeWorkDTO);
	boolean delete(StoreWorkDTO storeWorkDTO);
}
