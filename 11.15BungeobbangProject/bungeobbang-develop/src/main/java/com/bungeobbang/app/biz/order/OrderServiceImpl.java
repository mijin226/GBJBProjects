package com.bungeobbang.app.biz.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("orderService")
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDAO orderDAO;
	
	@Override
	public boolean insert(OrderDTO orderDTO) {
		return orderDAO.insert(orderDTO);
	}

	@Override
	public boolean update(OrderDTO orderDTO) {
		return orderDAO.update(orderDTO);
	}

	@Override
	public boolean delete(OrderDTO orderDTO) {
		return false;
	}

	@Override
	public ArrayList<OrderDTO> selectAll(OrderDTO orderDTO) {
		return (ArrayList<OrderDTO>) orderDAO.selectAll(orderDTO);
	}

	@Override
	public OrderDTO selectOne(OrderDTO orderDTOs) {
		return null;
	}
	
}
