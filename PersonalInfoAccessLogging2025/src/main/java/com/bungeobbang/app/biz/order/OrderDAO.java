package com.bungeobbang.app.biz.order;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	public boolean insert(OrderDTO orderDTO) {
		// 주문 내역 입력
		int rs = mybatis.insert("OrderDAO.insert",orderDTO);
		System.out.println("log : orderDTO pk = "+orderDTO.getOrderNum());
		return rs>0;
	}
	public boolean update(OrderDTO orderDTO) {
		// 주문 처리 업데이트
		int rs = mybatis.update("OrderDAO.update",orderDTO);
		return rs>0;
	}
	private boolean delete(OrderDTO orderDTO) {
		return false;
	}
	public List<OrderDTO> selectAll(OrderDTO orderDTO){
		List<OrderDTO> datas;
		if(orderDTO.getCondition() != null && orderDTO.getCondition().equals("SELECTALL_MEMBER")) {
			//회원으로 검색
			datas = mybatis.selectList("OrderDAO.selectAll_member",orderDTO);
		}
		else {
			// selectAll + adminChecked, desc 동적 수행
			datas = mybatis.selectList("OrderDAO.selectAll", orderDTO);						
		}

		for (OrderDTO order : datas) {
			System.out.println("data: " + order);
		}
		System.out.println("log : OrderDAO selectAll success");
		return datas;
	}
	private OrderDTO selectOne(OrderDTO orderDTO) {
		return null;
	}
}
