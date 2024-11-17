package com.bungeobbang.app.biz.payment;

import java.util.ArrayList;

public interface PaymentService {
	boolean insert(PaymentDTO paymentDTO);
	boolean update(PaymentDTO paymentDTO);
	boolean delete(PaymentDTO paymentDTO);
	ArrayList<PaymentDTO> selectAll(PaymentDTO paymentDTO);
	PaymentDTO selectOne(PaymentDTO paymentDTO);
}
