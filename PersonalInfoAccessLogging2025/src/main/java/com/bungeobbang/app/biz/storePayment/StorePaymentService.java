package com.bungeobbang.app.biz.storePayment;

import java.util.ArrayList;

public interface StorePaymentService {
	ArrayList<StorePaymentDTO> selectAll(StorePaymentDTO storePaymentDTO);
	StorePaymentDTO selectOne(StorePaymentDTO storePaymentDTO);
	boolean insert(StorePaymentDTO storePaymentDTO);
	boolean update(StorePaymentDTO storePaymentDTO);
	boolean delete(StorePaymentDTO storePaymentDTO);
}
