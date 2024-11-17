package com.bungeobbang.app.biz.storePayment;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//storePaymentService 구현체
@Service("storePaymentService")
public class StorePaymentServiceImpl  implements StorePaymentService{
	
	@Autowired
	private StorePaymentDAO storePaymentDAO;
	
	@Override
	public ArrayList<StorePaymentDTO> selectAll(StorePaymentDTO storePaymentDTO) {
		return null;
	}

	@Override
	public StorePaymentDTO selectOne(StorePaymentDTO storePaymentDTO) {
		return storePaymentDAO.selectOne(storePaymentDTO);
	}

	@Override
	public boolean insert(StorePaymentDTO storePaymentDTO) {
		return this.storePaymentDAO.insert(storePaymentDTO);
	}

	@Override
	public boolean update(StorePaymentDTO storePaymentDTO) {
		return this.storePaymentDAO.update(storePaymentDTO);
	}

	@Override
	public boolean delete(StorePaymentDTO storePaymentDTO) {
		return false;
	}
	
}
