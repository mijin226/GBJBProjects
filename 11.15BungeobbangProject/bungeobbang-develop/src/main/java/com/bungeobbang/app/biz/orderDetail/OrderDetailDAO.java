package com.bungeobbang.app.biz.orderDetail;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailDAO {
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public boolean insert(OrderDetailDTO orderDetailDTO) {
		int rs = mybatis.insert("OrderDetailDAO.insert", orderDetailDTO);
		System.out.println("log : orderDetailNum = "+orderDetailDTO.getOrderDetailNum());
		return rs>0;
	}
}
