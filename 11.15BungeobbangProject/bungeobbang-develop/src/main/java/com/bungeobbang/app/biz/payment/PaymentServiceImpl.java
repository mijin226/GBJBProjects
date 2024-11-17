package com.bungeobbang.app.biz.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService{
	@Autowired
	private PaymentDAO paymentDAO;
	
	@Override
	public boolean insert(PaymentDTO paymentDTO) {
		return this.paymentDAO.insert(paymentDTO);
	}

	@Override
	public boolean update(PaymentDTO paymentDTO) {
		return this.paymentDAO.update(paymentDTO);
	}

	@Override
	public boolean delete(PaymentDTO paymentDTO) {
		return false;
	}

	@Override
	public ArrayList<PaymentDTO> selectAll(PaymentDTO paymentDTO) {
		return (ArrayList<PaymentDTO>) this.paymentDAO.selectAll(paymentDTO);
	}

	@Override
	public PaymentDTO selectOne(PaymentDTO paymentDTO) {
		return null;
	}

}
