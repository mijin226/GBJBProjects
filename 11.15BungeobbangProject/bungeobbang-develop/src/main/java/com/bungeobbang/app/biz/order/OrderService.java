package com.bungeobbang.app.biz.order;

import java.util.ArrayList;

public interface OrderService {
	boolean insert(OrderDTO orderDTO);
	boolean update(OrderDTO orderDTO);
	boolean delete(OrderDTO orderDTO);
	ArrayList<OrderDTO> selectAll(OrderDTO orderDTO);
	OrderDTO selectOne(OrderDTO orderDTOs);
}
